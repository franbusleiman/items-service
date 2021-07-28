package com.busleiman.items.controllers;


import com.busleiman.items.domain.Item;
import com.busleiman.items.domain.Product;
import com.busleiman.items.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @HystrixCommand(fallbackMethod = "getProductFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")})
    @GetMapping(value = "/{id}/quantity/{quantity}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> getProductById(@PathVariable("id") Long id,
                                               @PathVariable("quantity") int quantity){

        logger.info("Looking for an item product");

        return ResponseEntity.ok(itemService.findById(id, quantity));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Item>> getItems(){

        return ResponseEntity.ok(itemService.findAll());
    }

    public ResponseEntity<Item> getProductFallback(Long id, int quantity){

       Product product = new Product(0L, "This is a fallback", LocalDate.from(Instant.now()), 0.00);

        return ResponseEntity.status(500).body(new Item(product, 0));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws Exception {
        itemService.deleteProduct(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws Exception {

        Product product1 = itemService.createProduct(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product1.getId())
                .toUri();

        return ResponseEntity.created(location).body(product1);
    }
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
                                                 @RequestBody Product product) throws Exception {

        return ResponseEntity.ok(itemService.updateProduct(id, product));
    }


}
