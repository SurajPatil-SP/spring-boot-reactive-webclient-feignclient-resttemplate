package com.main.sbp.feign;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.main.sbp.entity.Product;

@FeignClient(value = "productsBlocking", url = "http://localhost:8080")
public interface IProductService {
	
	Logger log = LoggerFactory.getLogger(IProductService.class);

	@GetMapping(path = "/slow-service-products", produces = "application/json")
    List<Product> getProductsBlocking(URI baseUrl);
	
	default List<Product> getProductsBlockingFallback(Exception exception) {
		log.info("in IProductService fallback.. {}", exception.getLocalizedMessage());
		return null;
	}
}
