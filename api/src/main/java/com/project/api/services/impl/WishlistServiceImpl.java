package com.project.api.services.impl;

import com.project.api.dtos.ProductFindAllDTO;
import com.project.api.entities.Account;
import com.project.api.entities.Product;
import com.project.api.repositories.AccountRepository;
import com.project.api.repositories.ProductRepository;
import com.project.api.services.AccountService;
import com.project.api.services.ProductService;
import com.project.api.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private AccountRepository accountRepository;

    private ProductRepository productRepository;

    @Autowired
    public WishlistServiceImpl(AccountRepository accountRepository, ProductRepository productRepository) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Boolean add(Integer accountId, Integer productId) {
        try {
            if(!this.isProductExist(accountId, productId)) {
                Account account = accountRepository.findById(accountId).get();
                Product product = productRepository.findById(productId).get();

                account.getWishlists().add(product);
                product.getWishlists().add(account);

                accountRepository.save(account);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean remove(Integer accountId, Integer productId) {
        try {
            if(this.isProductExist(accountId, productId)) {
                Account account = accountRepository.findById(accountId).get();
                Product product = productRepository.findById(productId).get();

                product.getWishlists().removeIf(acc -> acc.getId().compareTo(accountId) == 0);
                account.getWishlists().removeIf(pro -> pro.getProductId().compareTo(productId) == 0);

                accountRepository.save(account);
                productRepository.save(product);
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isProductExist(Integer accountId, Integer productId) {
        try {
            return accountRepository.isWishlistExist(accountId.toString(), productId.toString()) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
