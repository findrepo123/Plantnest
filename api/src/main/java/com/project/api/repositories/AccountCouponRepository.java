package com.project.api.repositories;

import com.project.api.entities.AccountCoupon;
import com.project.api.entities.AccountCouponId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "accountCoupons", path="account-coupons")
public interface AccountCouponRepository extends JpaRepository<AccountCoupon, AccountCouponId> {
}
