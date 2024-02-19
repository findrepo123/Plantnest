package com.project.api.specifications;

import com.project.api.entities.*;
import com.project.api.enumerations.OrderType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilters(
            String catalogName, String sizeName, String levelName, String orderBy, String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchTerm != null && !searchTerm.isBlank()) {
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + searchTerm + "%"));
            }

            if (catalogName != null && !catalogName.isBlank()) {
                Join<Product, Catalog> catalogJoin = root.join("catalog");
                predicates.add(criteriaBuilder.equal(catalogJoin.get("catalogName"), catalogName));
            }

            if (sizeName != null && !sizeName.isBlank()) {
                Join<Product, ProductVariant> variantJoin = root.joinSet("productVariants");
                Join<ProductVariant, ProductSize> sizeJoin = variantJoin.join("productSize");
                predicates.add(criteriaBuilder.equal(sizeJoin.get("sizeName"), sizeName));
            }

            if (levelName != null && !levelName.isBlank()) {
                Join<Product, PlantingDifficultyLevel> levelJoin = root.join("plantingDifficultyLevel");
                predicates.add(criteriaBuilder.equal(levelJoin.get("level"), levelName));
            }

            Order order = null;
            if (orderBy != null && !orderBy.isBlank()) {
                if (orderBy.equals(OrderType.MOST_RATED.getValue())) {
                    Subquery<Double> subquery = query.subquery(Double.class);
                    Root<ProductReview> subqueryRoot = subquery.from(ProductReview.class);

                    Join<Product, ProductReview> join = root.joinSet("productReviews");
                    Expression<Double> avgRating = criteriaBuilder.avg(subqueryRoot.get("rating"));

                    subquery.select(avgRating)
                            .where(criteriaBuilder.equal(join.get("product").get("productId"), root.get("productId")));
                    order = criteriaBuilder.desc(subquery);
                } else if (orderBy.equals(OrderType.BEST_SELLER.getValue())) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<OrderDetail> subqueryRoot = subquery.from(OrderDetail.class);
                    subquery.select(criteriaBuilder.sum(subqueryRoot.get("quantity")))
                            .where(criteriaBuilder.equal(subqueryRoot.get("product").get("productId"), root.get("productId")));
                    order = criteriaBuilder.desc(subquery);
                } else if(orderBy.equals(OrderType.SALE.getValue())) {
                    order = criteriaBuilder.desc(root.get("sale"));
                } else if(orderBy.equals(OrderType.TOP.getValue())) {
                    order = criteriaBuilder.desc(root.get("top"));
                } else {
                    order = criteriaBuilder.asc(root.get("productId"));
                }
            }
//            combines the predicates
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));

            query.where(finalPredicate);
            if(order != null) query.orderBy(order);
            return finalPredicate;
        };

    }
}
