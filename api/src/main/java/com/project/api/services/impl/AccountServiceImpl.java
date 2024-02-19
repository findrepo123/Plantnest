package com.project.api.services.impl;

import com.project.api.domain.UserPrincipal;
import com.project.api.dtos.*;
import com.project.api.entities.Account;
import com.project.api.entities.Contact;
import com.project.api.entities.Image;
import com.project.api.enumerations.Role;
import com.project.api.exceptions.domain.*;
import com.project.api.repositories.AccountRepository;
import com.project.api.repositories.ContactRepository;
import com.project.api.repositories.RoleRepository;
import com.project.api.services.AccountService;
import com.project.api.services.ProductService;
import com.project.api.utilities.ImageUploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.api.constants.FileConstant.*;
import static com.project.api.constants.FileConstant.NOT_AN_IMAGE_FILE;
import static com.project.api.constants.UserImplConstant.*;
import static com.project.api.enumerations.Role.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private ImageUploadUtils imageUploadUtils;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private ContactRepository contactRepository;
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository, ImageUploadUtils imageUploadUtils, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, ContactRepository contactRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.imageUploadUtils = imageUploadUtils;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.contactRepository = contactRepository;
    }


    @Override
    public List<AccountDTO> findAll() {
        try {
            List<Account> accounts = accountRepository.findAll();
            return accounts.stream().map(AccountDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Account findByEmail(String email) {
        try {
            return accountRepository.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Account findByUsername(String username) {
        try {
            return accountRepository.findAccountByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Account create(Account account) {
        try {
            String imgFileName = imageUploadUtils.uploadImgBase64("account", account.getImage());
            account.getImage().setImageUrl(imgFileName);
            account.setRole(roleRepository.findById(2).get());
            account.setPassword(encodePassword(account.getPassword()));
            account.setCreatedAt(new Date());
            account.setUpdatedAt(new Date());
            return accountRepository.save(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean update(Account account) {
        try {
            Account oldAccount = accountRepository.findById(account.getId()).get();

            if(!account.getImage().getImageUrl().startsWith("http://")) {
                if(oldAccount.getImage() != null) {
                    imageUploadUtils.delete("account", oldAccount.getImage().getImageUrl());
                }
                String imgFileName = imageUploadUtils.uploadImgBase64("account", account.getImage());
                account.getImage().setImageUrl(imgFileName);
            } else {
//              did not upload new img
                if(oldAccount.getImage() != null) {
                    account.setImage(oldAccount.getImage());
                }
            }
//            does not change password
            if(account.getPassword() == null) {
                account.setPassword(oldAccount.getPassword());
            } else {
//                hash password
            }
            account.setCreatedAt(oldAccount.getCreatedAt());
            account.setUpdatedAt(new Date());
            account.setRole(oldAccount.getRole());
            this.accountRepository.save(account);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateActive(List<Account> accounts, Boolean active) {
        try {
            accounts.forEach(account -> {
                Account oldAccount = this.accountRepository.findById(account.getId()).get();
                oldAccount.setActive(active);
                this.accountRepository.save(oldAccount);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            Account account = this.accountRepository.findById(id).get();
            this.imageUploadUtils.delete("account", account.getImage().getImageUrl());
            this.accountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean checkEmailExisting(String email) {
        try {
            return accountRepository.existsByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean checkUsernameExisting(String username) {
        try {
            return accountRepository.existsByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<AccountDTO> findByEmailKeyword(String keyword) {
        try {
            return accountRepository.findByEmailStartingWith(keyword).stream()
                        .map(AccountDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public AccountDetailDTO findById(Integer accountId) {
        try {
            Account account = this.accountRepository.findById(accountId).get();
            List<AddressDTO> addresses = account.getAddresses().stream()
                    .map(AddressDTO::new)
                    .toList();
            List<ProductReviewDTO> productReviews = account.getProductReviews().stream()
                    .map(ProductReviewDTO::new)
                    .toList();
            List<OrderFindAllDTO> orders = account.getOrders().stream()
                    .map(OrderFindAllDTO::new)
                    .toList();
            AccountDetailDTO accountDetailDTO = new AccountDetailDTO(account);
            accountDetailDTO.setAddresses(addresses);
            accountDetailDTO.setProductReviews(productReviews);
            accountDetailDTO.setOrders(orders);
            return accountDetailDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AccountDTO register(String username, String email, String password, String fullName, String phoneNumber) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(encodePassword(password));
        account.setFullName(fullName);
        account.setPhoneNumber(phoneNumber);
        account.setActive(true);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());
        account.setImage(null);

        account.setRole(roleRepository.findById(2).get());
        return new AccountDTO(accountRepository.save(account));
    }

    @Override
    public List<ProductFindAllDTO> findAllWishlists(Integer accountId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            return account.getWishlists().stream()
                    .map(ProductFindAllDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Integer countWishlist(Integer accountId) {
        try {
            return accountRepository.countWishlistsByAccountId(accountId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Long count() {
        return accountRepository.count();
    }

    @Override
    public Boolean updateProfileImage(String username, MultipartFile image) throws IOException, NotAnImageFileException {
        Account account = accountRepository.findAccountByUsername(username);
        saveProfileImage(account, image);
        return true;
    }

    @Override
    public Boolean updateFullName(String username, String fullName) {
        Account account = accountRepository.findAccountByUsername(username);
        account.setFullName(fullName);
        accountRepository.save(account);
        return true;
    }

    @Override
    public Boolean updateInformation(String username, String fullName,
                                     String currentPassword, String newPassword) throws WrongPasswordException {
        if(currentPassword == null || currentPassword.equals("")) {
            System.out.println(currentPassword);
            return this.updateFullName(username, fullName);
        }

        Account account = this.findByUsername(username);
        if(!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new WrongPasswordException("Wrong Password");
        }

        account.setPassword(encodePassword(newPassword));
        save(account);
        return true;
    }

    @Override
    public Boolean deleteContacts(List<Contact> contacts) {
        try {
            contacts.forEach(contact -> this.contactRepository.deleteById(contact.getContactId()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveProfileImage(Account account, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }

            String imgFileName = imageUploadUtils.upload("account", profileImage);
            if(account.getImage() != null) {
                imageUploadUtils.delete("account", account.getImage().getImageUrl());
                account.getImage().setImageUrl(imgFileName);
            }

            account.setImage(new Image(imgFileName));
            accountRepository.save(account);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(account);
//            user.setLastLoginDateDisplay(user.getLastLoginDate());
//            user.setLastLoginDate(new Date());
            accountRepository.save(account);
            UserPrincipal userPrincipal = new UserPrincipal(account);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(Account account) {
        if(account.getActive()) {
            if(loginAttemptService.hasExceededMaxAttempts(account.getUsername())) {
                account.setActive(false);
            } else {
                account.setActive(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(account.getUsername());
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Account validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        Account userByNewUsername = findByUsername(newUsername);
        Account userByNewEmail = findByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            Account currentUser = findByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
}
