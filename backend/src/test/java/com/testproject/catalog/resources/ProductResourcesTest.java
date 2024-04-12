package com.testproject.catalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testproject.catalog.dtos.ProductDTO;
import com.testproject.catalog.servicies.ProductService;
import com.testproject.catalog.servicies.exceptions.ResourcesNotFoundException;
import com.testproject.catalog.tests.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResources.class)
public class ProductResourcesTest {

    //Para fazer requisições e chamar o end point.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private ProductDTO dto;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2L;

        dto = ProductFactory.createNewProductDto();
        page = new PageImpl<>(List.of(dto));

        //SIMULAÇÃO DO FINDBYID DO SEVICE QUANDO O ID EXISTE
        when(service.findById(existingId)).thenReturn(dto);

        //SIMULAÇÃO DO FINDBYID DO SERVICE QUANDO O ID NÃO EXISTE
        when(service.findById(nonExistingId)).thenThrow(ResourcesNotFoundException.class);

        //Simulação do método findAllPaged
        when(service.findAllPaged(nonExistingId, any())).thenReturn(page);

        //Simulação do update quando o id existe
        when(service.update(eq(existingId), any())).thenReturn(dto);

        //Simulação do método update quando o id Não existe
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourcesNotFoundException.class);
    }

    //Teste para ver se o método findAllPaged do resources está retornando um page com o conteúdo
    @Test
    public void findAllPagedShouldReturnPage() throws Exception{
        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnPoductWhenIdExists() throws Exception{
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        //Teste para ver se aquele objeto realmente existe dizendo que no corpo da resposta tem que existir um campo id (atributos diversos da classe)
        result.andExpect(jsonPath("$.id").exists());

    }

    //teste para ver se quando o id não existir vai apresentar a página not found
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }
}
