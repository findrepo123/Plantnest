package com.project.api.controllers.customer;

import com.project.api.dtos.ProductFindAllDTO;
import com.project.api.services.AccountService;
import com.project.api.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishlists")
    public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private AccountService accountService;

    @GetMapping("findAll/{accountId}")
    public ResponseEntity<List<ProductFindAllDTO>> findAll(@PathVariable Integer accountId) {
        try {
            return new ResponseEntity<>(accountService.findAllWishlists(accountId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("count/{accountId}")
    public ResponseEntity<Integer> count(@PathVariable Integer accountId) {
        return new ResponseEntity<>(accountService.countWishlist(accountId), HttpStatus.OK);
    }

    @GetMapping("add")
    public ResponseEntity<Boolean> add(@RequestParam Integer accountId, @RequestParam Integer productId) {
        try {
            return new ResponseEntity<>(wishlistService.add(accountId, productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("remove")
    public ResponseEntity<Boolean> remove(@RequestParam Integer accountId, @RequestParam Integer productId) {
        try {
            return new ResponseEntity<>(wishlistService.remove(accountId, productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("isExists")
    public ResponseEntity<Boolean> isExists(@RequestParam Integer accountId, @RequestParam Integer productId) {
        try {
            return new ResponseEntity<>(wishlistService.isProductExist(accountId, productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
