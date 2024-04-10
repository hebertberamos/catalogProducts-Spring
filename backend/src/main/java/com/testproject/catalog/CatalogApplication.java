package com.testproject.catalog;

import com.testproject.catalog.dtos.UserNameProjectionDTO;
import com.testproject.catalog.projections.UserNameProjection;
import com.testproject.catalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogApplication /*implements CommandLineRunner*/ {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	/*@Override
	public void run(String... args) throws Exception {
		UserNameProjection obj = userRepository.search1(2);
		UserNameProjectionDTO dto = new UserNameProjectionDTO(obj);

		System.out.println("User name: " + dto.getEmail());
	}*/
}
