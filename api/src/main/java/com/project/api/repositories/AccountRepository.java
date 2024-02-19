package com.project.api.repositories;

import com.project.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "accounts", path="accounts")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Account> findByEmailStartingWith(String keyword);

    Account findByEmail(String email);

    Account findAccountByUsername(String username);

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query(value = "INSERT INTO Wishlist (account_id, product_id) VALUES (:accountId, :productId)", nativeQuery = true)
//    void insertWishlist(@Param("accountId") Integer accountId, @Param("productId") Integer productId);

//    @Modifying
//    @Query(value = "DELETE FROM Wishlist WHERE accountId=:accountId AND productId=:productId)", nativeQuery = true)
//    void removeWishlist(@Param("accountId") String accountId, @Param("productId") String productId);
//
    @Query(value = "SELECT CASE WHEN COUNT(account_id) > 0 THEN 1 ELSE 0 END FROM Wishlist " +
            "WHERE account_id=:accountId AND product_id=:productId", nativeQuery = true)
    Integer isWishlistExist(@Param("accountId") String accountId, @Param("productId") String productId);

    @Query("SELECT COUNT(w) FROM Account a JOIN a.wishlists w WHERE a.id = :accountId")
    Integer countWishlistsByAccountId(@Param("accountId") Integer accountId);
}
