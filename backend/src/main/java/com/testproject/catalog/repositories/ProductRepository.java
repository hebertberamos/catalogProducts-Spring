package com.testproject.catalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.testproject.catalog.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "INNER JOIN p.categories c " +
            "WHERE (c.id IN :categoriesId OR :categoriesId IS NULL) " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT ('%',:name,'%')) OR p.name = '') " +
            "ORDER BY p.id")
    Page<Product> findProducts(List<Long> categoriesId, String name, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p IN :products")
    List<Product> findProductsWithCategories(List<Product> products);
}
