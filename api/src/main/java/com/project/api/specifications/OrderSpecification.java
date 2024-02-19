package com.project.api.specifications;

import com.project.api.entities.*;
import com.project.api.enumerations.OrderType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<com.project.api.entities.Order> withFilters(Integer accountId, String searchTerm, String orderBy) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("account").get("id"), accountId));

            if (searchTerm != null && !searchTerm.isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("orderTrackingNumber"), searchTerm + "%"));
            }

            Order order = null;
            if (orderBy != null && !orderBy.isBlank()) {
                if (orderBy.equals(OrderType.TOTAL_PRICE.getValue())) {
                    order = criteriaBuilder.desc(root.get("totalPrice"));
                } else if (orderBy.equals(OrderType.NEWEST.getValue())) {
                    order = criteriaBuilder.desc(root.get("createdAt"));
                } else {
                    order = criteriaBuilder.asc(root.get("orderId"));
                }
            }
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));

            query.where(finalPredicate);
            if(order != null) query.orderBy(order);
            return finalPredicate;
        };

    }
}
