package com.project.api.services;

import com.project.api.dtos.AccountDTO;
import com.project.api.dtos.AccountDetailDTO;
import com.project.api.dtos.ProductFindAllDTO;
import com.project.api.entities.Account;
import com.project.api.entities.Contact;
import com.project.api.entities.Product;
import com.project.api.exceptions.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();
    Account findByEmail(String email);
    Account findByUsername(String username);
    Account create(Account account);
    Boolean update(Account account);
    Boolean updateActive(List<Account> accounts, Boolean active);
    Boolean deleteById(Integer id);
    Boolean checkEmailExisting(String email);
    Boolean checkUsernameExisting(String username);
    List<AccountDTO> findByEmailKeyword(String keyword);
    Account save(Account account);
    AccountDetailDTO findById(Integer accountId);
    AccountDTO register(String username, String email, String password, String fullName, String phoneNumber) throws UserNotFoundException, EmailExistException, UsernameExistException;
    List<ProductFindAllDTO> findAllWishlists(Integer accountId);
    Integer countWishlist(Integer accountId);
    Long count();
    Boolean updateProfileImage(String username, MultipartFile image) throws IOException, NotAnImageFileException;
    Boolean updateFullName(String username, String fullName);
    Boolean updateInformation(String username, String fullName, String currentPassword, String newPassword) throws WrongPasswordException;
    Boolean deleteContacts(List<Contact> contacts);
}
