package com.testproject.catalog.servicies;

import com.testproject.catalog.dtos.ProductDTO;
import com.testproject.catalog.entities.Product;
import com.testproject.catalog.repositories.ProductRepository;
import com.testproject.catalog.servicies.exceptions.DatabaseException;
import com.testproject.catalog.servicies.exceptions.ResourcesNotFoundException;
import com.testproject.catalog.tests.ProductFactory;
import net.bytebuddy.agent.VirtualMachine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

//Testes de Unidade da classe ProductService
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = ProductFactory.createNewProduct();
        page = new PageImpl<>(List.of(product));



        //Comportamento simulado de tentar Encontrar todos os elementos do banco de dados - me retorna um page contendo uma "lista" com o elemento que eu adiconei a ele.
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Comportamento simulado de tentar salvar um elemento dentro do banco de dados - Me retorna o objeto que foi criado por mim dentro do método setUp()
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        //Comportamento simulado de tentar encontrar um elemento pelo id. (quando o id existe) - Me retorna um objeto do tipo Optional com o objeto product passado para ele
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        //Comportamento simulado de tentar encontrar um elemento pelo id (quando o id não existe)   Me retornar uma objeto Optional vazio
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Comportamento simulado de deletar um elemento do banco de dados a partir do id e não fazer nada - O método aqui é void, então ele não retorna nada, apenas faz a ação.
        Mockito.doNothing().when(repository).deleteById(existingId);

        //Comportamento simulado de tentar deletar um elmento pelo id e lançar uma exceção, caso não exista um elemento com aquele id - O método aqui é void, então ele não retorna nada, apenas faz a ação.
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        //Comportamente simulado de tentar deletar um elemento pelo id e lançar a exceção atabaseException quando o elemento estiver associado a outro - O método aqui é void, então ele não retorna nada, apenas faz a ação.
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
       Assertions.assertDoesNotThrow(() -> {
           service.deleteById(existingId);
       });

        //Assertion do Mokito pra verificar se algum método específico de Mock(repositry) foi chamado
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThowEmptyResultDataAccessExceptionWhenIdNonExists() {
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            service.deleteById(nonExistingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    //Violando a integridade do banco, tentando deletar um objeto que esá interado com outro e não podia ser deletado
    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.deleteById(dependentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    //Testando para ver se o método findAll está retornando o Page com os objetos salvos dentro dele
    @Test
    public void saveShouldReturnPageWithElementsInsideHimWhenExistElements(){
        Pageable pageable = PageRequest.of(0, 10);
        Long categoryId = 1L;
        Page<ProductDTO> result = service.findAllPaged(categoryId, pageable);

        Assertions.assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnAProductDTOWhenIdExist(){
        ProductDTO dto = service.findById(existingId);

        Assertions.assertNotNull(dto);

        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourcesNotFoundExceptionWhenIdNonExist(){
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            ProductDTO dto = service.findById(nonExistingId);
        });

        Mockito.verify(repository).findById(nonExistingId);
    }
}

