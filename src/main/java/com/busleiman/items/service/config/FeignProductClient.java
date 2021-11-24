package com.busleiman.items.service.config;

import com.busleiman.items.domain.dtos.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "products-service")
public interface FeignProductClient {

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    List<Product> getProducts();

    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, value = "/products")
    ResponseEntity<Product>createProduct(@RequestBody Product product);

    @RequestMapping(method = RequestMethod.PUT, value = "/products/{id}")
    ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
                           @RequestBody Product product);

    @RequestMapping(method = RequestMethod.DELETE, value = "/products/{id}")
    Void deleteProduct(@PathVariable("id") Long id);


}
