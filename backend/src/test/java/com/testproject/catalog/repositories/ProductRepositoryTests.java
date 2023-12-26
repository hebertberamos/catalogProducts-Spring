package com.testproject.catalog.repositories;

import com.testproject.catalog.entities.Category;
import com.testproject.catalog.entities.Product;
import com.testproject.catalog.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long unexcitingId;

    @BeforeEach
    public void setUp() throws Exception{
        existingId = 1L;
        unexcitingId = 1000L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist(){
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    //Ver o porque de a exceção não estar sen lançada quando o id do método deletById não existir dentro do banco de dados
//    @Test
//    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
//        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
//            repository.deleteById(unexcitingId);
//        });
//    }

    //Testando para ver se oméodo save está adicionando um novo objeto quando o id é nulo
    @Test
    public void saveShouldAddObjectWhenIdIsNull(){
        Product prod = ProductFactory.createNewProduct("TV 50' Phillips", 1000.0, Instant.now(), "qualquer coisinha aqui", "quialquer coisa aqui");

        repository.save(prod);

        Assertions.assertNotNull(prod.getId());
    }

}
