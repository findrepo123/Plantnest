package com.project.api.controllers.customer;


import com.project.api.dtos.CartDTO;
import com.project.api.dtos.request.CartRequestDTO;
import com.project.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("findAll")
    public ResponseEntity<CartDTO> findAll(@RequestParam Integer accountId) {
        try {
            return new ResponseEntity<>(cartService.findAll(accountId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("canAddToCart")
    public ResponseEntity<Boolean> canAddToCart(@RequestBody CartRequestDTO item) {
        try {
            Boolean result = cartService.canAddToCart(item.getAccountId(), item.getProductId(), item.getProductVariantId(), item.getQuantity());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add")
    public ResponseEntity<Boolean> add(@RequestBody CartRequestDTO item) {
        try {
            Boolean result = cartService.save(item.getAccountId(), item.getProductId(), item.getProductVariantId(), item.getQuantity());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("remove")
    public ResponseEntity<Boolean> remove(@RequestBody CartRequestDTO item) {
        try {
            Boolean result = cartService.remove(item.getAccountId(), item.getProductId(), item.getProductVariantId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("clear")
    public ResponseEntity<Boolean> clear(@RequestParam Integer accountId) {
        try {
            Boolean result = cartService.removeAll(accountId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
