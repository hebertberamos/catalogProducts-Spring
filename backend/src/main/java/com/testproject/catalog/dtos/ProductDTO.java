package com.testproject.catalog.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.testproject.catalog.entities.Category;
import com.testproject.catalog.entities.Product;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Double price;
	private Instant date;
	private String description;
	private String imgUrl;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {
	}

	public ProductDTO(Long id, String name, Double price, Instant date, String description, String imgUrl) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.date = date;
		this.description = description;
		this.imgUrl = imgUrl;
	}
	
	//Para instanciar um ProductDTO a partir de uma entidade Product
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.price = entity.getPrice();
		this.date = entity.getDate();
		this.description = entity.getDescription();
		this.imgUrl = entity.getImgUrl();
	}
	
	//Para instanciar um ProductDTO passando também as categorias que ele tem.
	public ProductDTO(Product entity, List<Category> categories) {
		this(entity);
		//adicionando as entidades que foram passadas pela listo do parâmetro e adicionar dentro da lista do objeto.
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}
}
