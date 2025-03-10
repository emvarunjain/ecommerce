package com.example.InventoryService;

import com.example.InventoryService.repository.InventoryRepository;
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
public class InventoryServiceApplicationTests {

	@Container
	static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.0.28");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private InventoryRepository inventoryRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
	}

	@Test
	void shouldAddInventory() throws Exception {
		inventoryRepository.deleteAll();
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/inventory")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, inventoryRepository.findAll().size());
		inventoryRepository.deleteAll();
	}
}
