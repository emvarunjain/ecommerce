package com.example.OrderService;

import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItem;
import com.example.OrderService.repository.OrderRepository;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class OrderServiceApplicationTests {

	@Container
	static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.0.28");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private OrderRepository orderRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
	}

	@Test
	void shouldCreateOrder() throws Exception {
		orderRepository.deleteAll();
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"lineItems\":[{\"skuCode\":\"123\",\"quantity\":1,\"price\":10.0}]}"))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, orderRepository.findAll().size());
		orderRepository.deleteAll();
	}

	@Test
	void shouldGetAllOrders() throws Exception {
		orderRepository.save(Order.builder()
						.id(123L)
						.lineItems(List.of(OrderLineItem.builder()
								.skuCode("123")
								.quantity(1)
								.price(10.0)
								.build()))
				.build());
		String result = mockMvc.perform(MockMvcRequestBuilders
						.get("/api/order")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
	}
}
