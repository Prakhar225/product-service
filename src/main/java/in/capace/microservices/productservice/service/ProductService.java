package in.capace.microservices.productservice.service;

import java.util.List;

import in.capace.microservices.productservice.dto.ProductRequest;
import in.capace.microservices.productservice.dto.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.capace.microservices.productservice.model.Product;
import in.capace.microservices.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	@Autowired
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        var mapper = new ModelMapper();
        Product product = mapper.map(productRequest, Product.class);
        productRepository.save(product);
        log.info("Created product {}", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        var mapper = new ModelMapper();
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mapper.map(product, ProductResponse.class)).toList();
    }

}
