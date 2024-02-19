package com.project.api.repositories;

import com.project.api.entities.Coupon;
import com.project.api.entities.projection.CouponProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@RepositoryRestResource(collectionResourceRel = "coupons", path="coupons",
        excerptProjection = CouponProjection.class)
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    boolean existsByCode(String code);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Coupon c " +
            "WHERE c.code = :code AND :currentDate BETWEEN c.startedAt AND c.expiredAt ")
    boolean isCouponCanBeUsed(String code, Date currentDate);

    Coupon findCouponByCode(String code);

}
