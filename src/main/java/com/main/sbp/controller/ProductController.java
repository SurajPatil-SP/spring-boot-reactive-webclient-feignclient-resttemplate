package com.main.sbp.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.main.sbp.entity.Product;
import com.main.sbp.feign.IProductService;

import reactor.core.publisher.Flux;

@RestController
public class ProductController {
	
	Logger log = LoggerFactory.getLogger(ProductController.class);
	
	private static final int SERVER_PORT = 8080;
	
	private static final String PRODUCT_ENDPOINT_NAME = "/slow-service-products";
	
	@Autowired
	private IProductService productService;

	@GetMapping("/slow-service-products")
    private List<Product> getAllProducts() throws  InterruptedException {
        Thread.sleep(2000L); // delay
        return Arrays.asList(
          new Product("Fancy Smartphone", "A stylish phone you need"),
          new Product("Cool Watch", "The only device you need"),
          new Product("Smart TV", "Cristal clean images")
        );
    }
	
	@GetMapping("/products-blocking")
	public List<Product> getProductsBlocking() {
		log.info("Starting BLOCKING Controller!");
		final URI uri = URI.create(baseUri());
		List<Product> result = productService.getProductsBlocking(uri);
		result.forEach(product -> log.info(product.toString()));
		log.info("Exiting BLOCKING Controller!");
		return result;
	}
	
	@GetMapping("/products-non-blocking")
	public Flux<Product> getProductsNonBlocking() {
		log.info("Starting NON-BLOCKING Controller!");
		Flux<Product> productFlux = WebClient.create()
				.get()
				.uri(baseUri() + PRODUCT_ENDPOINT_NAME)
				.retrieve()
				.bodyToFlux(Product.class);
		
		productFlux.subscribe(product -> log.info(product.toString()));
		log.info("Exiting NON-BLOCKING Controller!");
		return productFlux;
	}
	
	@GetMapping("/products-blocking-rest-template")
	public List<Product> getProductsBlockingWithRestTemplate() {
		log.info("Starting BLOCKING Controller!");
		final URI uri = URI.create(baseUri());
		RestTemplate restTemplate = new RestTemplate ();
		ResponseEntity<List<Product>> exchange = restTemplate.exchange(
				uri + PRODUCT_ENDPOINT_NAME, HttpMethod.GET, null, 
				new ParameterizedTypeReference<List<Product>>() {});
		List<Product> result = exchange.getBody();
		result.forEach(product -> log.info(product.toString()));
		log.info("Exiting BLOCKING Controller!");
		return result;
	}
	
	private String baseUri () {
		return "http://localhost:" + SERVER_PORT;
	}
}
