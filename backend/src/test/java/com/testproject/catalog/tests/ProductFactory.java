package com.testproject.catalog.tests;

import com.testproject.catalog.dtos.ProductDTO;
import com.testproject.catalog.entities.Product;

import java.time.Instant;

public class ProductFactory {


    public static Product createNewProduct(String name, Double price, Instant date, String description, String imgUrl){
        return new Product(name, price, date, description, imgUrl);
    }

    public static ProductDTO createNewProductDTO(String name, Double price, Instant date, String description, String imgUrl){
        return new ProductDTO(name, price, date, description, imgUrl);
    }

}
