package com.project.api.controllers.customer;

import com.project.api.domain.UserPrincipal;
import com.project.api.dtos.AccountDTO;
import com.project.api.dtos.AddressDTO;
import com.project.api.entities.Account;
import com.project.api.exceptions.ExceptionHandling;
import com.project.api.exceptions.domain.*;
import com.project.api.services.AccountService;
import com.project.api.services.AddressService;
import com.project.api.utilities.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.List;

import static com.project.api.constants.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api")
public class CustomerController extends ExceptionHandling {
    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JWTTokenProvider jwtTokenProvider;
    private AddressService addressService;

    @Autowired
    public CustomerController(AuthenticationManager authenticationManager, AccountService accountService, JWTTokenProvider jwtTokenProvider, AddressService addressService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.addressService = addressService;
    }

    @PostMapping("/login")
    public ResponseEntity<AccountDTO> login(@RequestBody Account account) {
        authenticate(account.getUsername(), account.getPassword());
        Account loginUser = accountService.findByUsername(account.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(new AccountDTO(loginUser), jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AccountDTO> register(@RequestBody Account account) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        AccountDTO newAccount = accountService.register(account.getUsername(), account.getEmail(), account.getPassword(), account.getFullName(), account.getPhoneNumber());
        return new ResponseEntity<>(newAccount, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<AccountDTO> getUser(@PathVariable("username") String username) {
        return new ResponseEntity<>(new AccountDTO(accountService.findByUsername(username)), OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @GetMapping("isUsernameExist")
    public ResponseEntity<Boolean> isUsernameExist(@RequestParam("username") String username) {
        try {
            return new ResponseEntity<>(this.accountService.checkUsernameExisting(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("isEmailExist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(this.accountService.checkEmailExisting(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountId}/addresses")
    public ResponseEntity<List<AddressDTO>> findAddressesByAccountId(@PathVariable Integer accountId) {
        try {
            return new ResponseEntity<>(addressService.findByAccountId(accountId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<Boolean> updateProfileImage(
            @RequestParam("username") String username,
            @RequestParam(value = "profileImage") MultipartFile profileImage)
            throws IOException, NotAnImageFileException {
        return new ResponseEntity<>(accountService.updateProfileImage(username, profileImage), OK);
    }

    @PostMapping("/updateInformation")
    public ResponseEntity<Boolean> updateInformation(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword)
            throws WrongPasswordException {
        System.out.println(username + " - " + fullName + " - " + currentPassword + " - " + newPassword);
        return new ResponseEntity<>(accountService.updateInformation(username, fullName, currentPassword, newPassword), OK);
    }
}
