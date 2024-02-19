package com.project.api.controllers.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.dtos.AccountDTO;
import com.project.api.dtos.AccountDetailDTO;
import com.project.api.dtos.AddressDTO;
import com.project.api.entities.Account;
import com.project.api.entities.Contact;
import com.project.api.services.AccountService;
import com.project.api.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin/accounts")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AddressService addressService;

    @GetMapping("findAll")
    public ResponseEntity<List<AccountDTO>> findAll() {
        try {
            return new ResponseEntity<>(this.accountService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findById/{accountId}")
    public ResponseEntity<AccountDetailDTO> findById(@PathVariable Integer accountId) {
        try {
            return new ResponseEntity<>(accountService.findById(accountId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @PostMapping("create")
    public ResponseEntity<Boolean> create(@RequestBody Account account) {
        try {
            if(this.accountService.create(account) != null) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST)  ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update")
    public ResponseEntity update(@RequestBody Account account) {
        try {
            this.accountService.update(account);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Update Category Failed");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-status")
    public ResponseEntity<Boolean> updateStatus(
            @RequestParam("accounts") String accountsJson,
            @RequestParam("active") Boolean active) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Account> accounts = mapper.readValue(accountsJson, new TypeReference<List<Account>>() {});

            if(this.accountService.updateActive(accounts, active)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{accountId}")
    public ResponseEntity<Boolean> delete(@PathVariable("accountId") Integer accountId) {
        try {
            if(this.accountService.deleteById(accountId)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("findByEmailKeyword")
    public ResponseEntity<List<AccountDTO>> findByEmailKeyword(@RequestParam String keyword) {
        try {
            return new ResponseEntity<>(this.accountService.findByEmailKeyword(keyword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("count")
    public ResponseEntity<Long> countAccounts() {
        try {
            return new ResponseEntity<>(this.accountService.count(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-contacts")
    public ResponseEntity<Boolean> deleteContacts(@RequestBody List<Contact> contacts) {
        return new ResponseEntity<>(this.accountService.deleteContacts(contacts), HttpStatus.OK);
    }
}
