package com.example.ProductService;

import com.example.ProductService.model.Product;
import com.example.ProductService.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.0.28");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		productRepository.deleteAll();
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Test Product\",\"description\":\"Test description\",\"price\":100.0}"))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
		productRepository.deleteAll();
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		productRepository.save(Product.builder()
				.name("Sample Product")
				.description("Sample description")
				.price(100.0)
				.build());
		String result = mockMvc.perform(MockMvcRequestBuilders
						.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
	}
}
