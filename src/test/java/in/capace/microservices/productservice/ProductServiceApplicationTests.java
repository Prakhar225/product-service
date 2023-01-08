package in.capace.microservices.productservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import in.capace.microservices.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.capace.microservices.productservice.model.Product;
import in.capace.microservices.productservice.repository.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ProductRepository productRepository;
	// @MockBean
	// ProductService mockProductService;

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.3");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		String product = objectMapper.writeValueAsString(ProductRequest.builder()
				.name("Test Product")
				.description("This is a test product")
				.price(BigDecimal.valueOf(123456))
				.build());
		mockMvc.perform(post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(product))
				.andExpect(status().isCreated());

		Assertions.assertEquals(productRepository.findAll().size(), 2);
	}

	@Test
	void shouldFetchProduct() throws Exception {
		Product product = new Product("5", "Mock Product", "Mock Description", BigDecimal.valueOf(1234567));
		productRepository.save(product);
		mockMvc.perform(get("/api/product")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("Mock Product"))
				.andExpect(jsonPath("$[0].description").value("Mock Description"))
				.andExpect(jsonPath("$[0].price").value(1234567));
	}

}
