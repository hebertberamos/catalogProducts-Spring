package com.testproject.catalog.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.testproject.catalog.entities.Category;
import com.testproject.catalog.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	@Size(min = 5, max = 60, message = "O nome do produto deve ter no mínimo 5 caracteres e no máximo 60")
	@NotBlank(message = "Campo requerido")
	private String name;
	@Positive(message = "O preço deve ser um valor positivo")
	private Double price;
	@PastOrPresent(message = "A data não pode ser uma data futura")
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

	public ProductDTO(String name, Double price, Instant date, String description, String imgUrl){
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
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity);
		//adicionando as entidades que foram passadas pela listo do parâmetro e adicionar dentro da lista do objeto.
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}
}
