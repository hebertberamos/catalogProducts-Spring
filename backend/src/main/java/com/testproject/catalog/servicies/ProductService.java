package com.testproject.catalog.servicies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.testproject.catalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.testproject.catalog.dtos.ProductDTO;
import com.testproject.catalog.entities.Category;
import com.testproject.catalog.entities.Product;
import com.testproject.catalog.repositories.ProductRepository;
import com.testproject.catalog.servicies.exceptions.DatabaseException;
import com.testproject.catalog.servicies.exceptions.ResourcesNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
		List<Long> categoriesId = (categoryId == 0) ? null : Arrays.asList(categoryId);
		Page<Product> page = repository.findProducts(categoriesId, name, pageable);
		repository.findProductsWithCategories(page.getContent());
		return page.map(x -> new ProductDTO(x, x.getCategories()));
	}
	
	@Transactional
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product prod = obj.orElseThrow(() -> new ResourcesNotFoundException("Product no found"));
		
		return new ProductDTO(prod, prod.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product prod = new Product();
		prod.setName(dto.getName());
		prod.setPrice(dto.getPrice());
		prod.setDate(dto.getDate());
		prod.setDescription(dto.getDescription());
		prod.setImgUrl(dto.getImgUrl());
		for(Category cat : prod.getCategories()) {
			prod.addCategoryToList(cat);
		}
		
		
		prod = repository.save(prod);
		return new ProductDTO(prod, prod.getCategories());
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try { 
			Product prod = repository.findById(id).get();
			
			prod.setName(dto.getName());
			prod.setPrice(dto.getPrice());
			prod.setDate(dto.getDate());
			prod.setDescription(dto.getDescription());
			prod.setImgUrl(dto.getImgUrl());
			for(Category cat : prod.getCategories()) {
				prod.addCategoryToList(cat);
			}
			
			prod = repository.save(prod);
			return new ProductDTO(prod, prod.getCategories());
		}
		catch(EntityNotFoundException e) {
			throw new ResourcesNotFoundException("Id not found");
		}
	}
	
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourcesNotFoundException("Id not found (" + id + ")");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("This category can not be deleted. LINKED PRODUTCS");
		}
	}
}
