package com.testproject.catalog.servicies;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.testproject.catalog.dtos.CategoryDTO;
import com.testproject.catalog.entities.Category;
import com.testproject.catalog.repositories.CategoryRepository;
import com.testproject.catalog.servicies.exceptions.DatabaseException;
import com.testproject.catalog.servicies.exceptions.ResourcesNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourcesNotFoundException("Entity not found"));
		
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category cat = new Category();
		cat.setName(dto.getName());
		//para salvar a nova informação dentro do banco de dados
		cat = repository.save(cat);
		
		return new CategoryDTO(cat);
	}
	
	@Transactional
	public CategoryDTO update(CategoryDTO dto, Long id) {
		try { 
			Category entity = repository.findById(id).get();
			entity.setName(dto.getName());
			//para salvar a nova informação dentro do banco de dados
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourcesNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourcesNotFoundException("Id no found (" + id + ")");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("This category can not be deleted. LINKED PRODUTCS");
		}
	}
	
}
