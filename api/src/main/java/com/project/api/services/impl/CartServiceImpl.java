package com.project.api.services.impl;

import com.project.api.dtos.CartDTO;
import com.project.api.entities.*;
import com.project.api.repositories.AccountRepository;
import com.project.api.repositories.CartRepository;
import com.project.api.repositories.ProductRepository;
import com.project.api.repositories.ProductVariantRepository;
import com.project.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private AccountRepository accountRepository;

    private ProductRepository productRepository;

    private CartRepository cartRepository;

    private ProductVariantRepository variantRepository;

    @Autowired
    public CartServiceImpl(AccountRepository accountRepository, ProductRepository productRepository, CartRepository cartRepository, ProductVariantRepository variantRepository) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.variantRepository = variantRepository;
    }

    @Override
    public CartDTO findAll(Integer accountId) {
        Account account = accountRepository.findById(accountId).get();
        Cart cart = getCart(account);
        return new CartDTO(cart);
    }

    @Override
    public Boolean save(Integer accountId, Integer productId, Integer productVariantId, Integer quantity) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Product product = productRepository.findById(productId).get();
            ProductVariant variant = variantRepository.findById(productVariantId).get();
            Cart cart = getCart(account);

            if(isExists(cart.getCartId(), productId, productVariantId)) {
                CartDetail oldDetail = findCartDetail(cart.getCartId(), productId, productVariantId);
                oldDetail.setQuantity(quantity);
            } else {
                CartDetail cartDetail = new CartDetail(product, variant, quantity);
                cartDetail.setCart(cart);
                cart.getCartDetails().add(cartDetail);
            }
            cart.setUpdatedAt(new Date());
            cartRepository.save(cart);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private CartDetail findCartDetail(Integer cartId, Integer productId, Integer variantId) {
        Cart cart = cartRepository.findById(cartId).get();
        return cart.getCartDetails().stream()
                .filter(detail -> detail.getCart().getCartId().compareTo(cartId) == 0 &&
                        detail.getProduct().getProductId().compareTo(productId) == 0 &&
                        detail.getProductVariant().getProductVariantId().compareTo(variantId) == 0)
                .findFirst().get();
    }

    @Override
    public Boolean remove(Integer accountId, Integer productId, Integer productVariantId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Cart cart = getCart(account);
            if(isExists(cart.getCartId(), productId, productVariantId)) {
                CartDetail oldDetail = findCartDetail(cart.getCartId(), productId, productVariantId);
                cart.getCartDetails().removeIf(detail -> detail.getCartDetailId().compareTo(oldDetail.getCartDetailId()) == 0);
                cartRepository.save(cart);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isExists(Integer cartId, Integer productId, Integer productVariantId) {
        try {
            return cartRepository.isCartDetailExist(cartId, productId, productVariantId) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeAll(Integer accountId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Cart cart = account.getCart();
            if(cart == null) return true;

            cart.getCartDetails().clear();
            cartRepository.save(cart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean canAddToCart(Integer accountId, Integer productId, Integer productVariantId, Integer quantity) {
        try {
            ProductVariant variant = variantRepository.findById(productVariantId).get();
            return variant.getQuantity() > quantity;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Cart getCart(Account account) {
        Cart cart;
        if(account.getCart() == null) {
            cart = new Cart();
            cart.setCartId(account.getId());
            cart.setCreatedAt(new Date());
            cart.setUpdatedAt(new Date());
        } else {
            cart = account.getCart();
        }
        return cart;
    }
}
