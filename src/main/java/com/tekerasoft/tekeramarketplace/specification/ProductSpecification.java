package com.tekerasoft.tekeramarketplace.specification;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSpecification {
    public static Specification<Product> hasVariationAttributesWithOptionalModelName(
            String modelName,
            Map<String, String> attributeMap
    ) {
        return (root, query, cb) -> {
            query.distinct(true);

            Join<Object, Object> variationJoin = root.join("variations", JoinType.INNER);
            Join<Object, Object> attrJoin = variationJoin.join("attributes", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            // modelName null veya boş değilse filtrele
            if (modelName != null && !modelName.isEmpty()) {
                predicates.add(cb.equal(variationJoin.get("modelName"), modelName));
            }

            // attribute filtreleri
            for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
                Predicate keyPredicate = cb.equal(attrJoin.get("key"), entry.getKey());
                Predicate valuePredicate = cb.equal(attrJoin.get("value"), entry.getValue());
                predicates.add(cb.and(keyPredicate, valuePredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
