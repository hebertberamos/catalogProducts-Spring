package com.testproject.catalog.tests;

import com.testproject.catalog.dtos.ProductDTO;
import com.testproject.catalog.entities.Category;
import com.testproject.catalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createNewProduct() {
        Product prod = new Product(1L, "Phone", 800.0, Instant.now(), "qualquer coisa já que é string", "qualquer coisa aqui tb, já que vai aceitar qualquer coisa pq é uma string");
        prod.getCategories().add(new Category(2L, "Electronics"));

        return prod;
    }

    public static ProductDTO createNewProductDto() {
        Product prod = createNewProduct();
        return new ProductDTO(prod, prod.getCategories());
    }
}
