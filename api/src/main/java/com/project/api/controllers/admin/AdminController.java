package com.project.api.controllers.admin;

import com.project.api.domain.HttpResponse;
import com.project.api.domain.UserPrincipal;
import com.project.api.dtos.AccountDTO;
import com.project.api.entities.Account;
import com.project.api.enumerations.Role;
import com.project.api.exceptions.domain.EmailExistException;
import com.project.api.exceptions.domain.NotAnImageFileException;
import com.project.api.exceptions.domain.UserNotFoundException;
import com.project.api.exceptions.domain.UsernameExistException;
import com.project.api.services.AccountService;
import com.project.api.utilities.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.IOException;

import static com.project.api.constants.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public AdminController(AuthenticationManager authenticationManager, AccountService accountService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AccountDTO> login(@RequestBody Account account) {
        authenticate(account.getUsername(), account.getPassword());
        Account loginAdmin = accountService.findByUsername(account.getUsername());
        if(loginAdmin.getRole().getName().equals(Role.ROLE_USER.name())) {
            throw new AccessDeniedException("");
        }
        UserPrincipal userPrincipal = new UserPrincipal(loginAdmin);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(new AccountDTO(loginAdmin), jwtHeader, OK);
    }

    @GetMapping("/find/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Account> getUser(@PathVariable("username") String username) {
        Account account = accountService.findByUsername(username);
        return new ResponseEntity<>(account, OK);
    }


    @PostMapping("/updateProfileImage")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateProfileImage(
            @RequestParam("username") String username,
            @RequestParam(value = "profileImage") MultipartFile profileImage)
            throws IOException, NotAnImageFileException {
        return new ResponseEntity<>(accountService.updateProfileImage(username, profileImage), OK);
    }

    @PostMapping("/updateFullName")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateFullName(
            @RequestParam("username") String username,
            @RequestParam("fullName") String fullName) {
        return new ResponseEntity<>(accountService.updateFullName(username, fullName), OK);
    }


    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
