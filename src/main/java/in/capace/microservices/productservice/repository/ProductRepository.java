package in.capace.microservices.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import in.capace.microservices.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {    
}
