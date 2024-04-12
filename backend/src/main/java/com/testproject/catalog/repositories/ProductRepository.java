package com.testproject.catalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.testproject.catalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "INNER JOIN p.categories c " +
            "WHERE c.id = :categoryId OR :categoryId = 0 ORDER BY p.id ")
    Page<Product> findProducts(Long categoryId, Pageable pageable);

}
