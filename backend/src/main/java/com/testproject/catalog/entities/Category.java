package com.testproject.catalog.entities;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "category_DB")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column(name = "Name")
	private String name;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", name = "created_At")
	private Instant createdAt;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", name = "updated_At")
	private Instant updatedAt;
	
	
	public Category() {
	}

	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	
	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
	}
	
	@PreUpdate
	private void preUpdate() {
		updatedAt = Instant.now();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	
	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
}
