package org.lucca.shopvalidator.repository;

import org.lucca.shopvalidator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductIdentifier(String identifier);
}
