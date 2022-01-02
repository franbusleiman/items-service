package com.busleiman.items.controllers;


import brave.Response;
import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.responses.ItemResponse;
import com.busleiman.items.domain.entities.Item;
import com.busleiman.items.domain.dtos.Product;
import com.busleiman.items.domain.validationsGroups.Action;
import com.busleiman.items.service.ItemServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemServiceImpl itemService;

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @HystrixCommand(fallbackMethod = "getProductFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")})
    @GetMapping(value = "/{id}/quantity/{quantity}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> getProductById(@PathVariable("id") Long id){

        logger.info("Looking for an item product");

        return ResponseEntity.ok(itemService.findById(id));
    }

    @GetMapping(value = "/sorted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponse>> findAllItemsSortedByPrice(){

        return ResponseEntity.ok().body(itemService.findAllSortedByPrice());
    }

    @GetMapping(value = "/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponse>> findItemsWhitHigherPrice(@RequestParam("price") Double price){

        return ResponseEntity.ok().body(itemService.findAllWithHigherPrice(price));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemResponse>> getItems(){

        return ResponseEntity.ok(itemService.findAll());
    }

    public ResponseEntity<ItemResponse> getProductFallback(Long id, int quantity){

       Product product = new Product(0L, "This is a fallback", LocalDate.from(Instant.now()), 0.00);

        return ResponseEntity.status(500).body(ItemResponse.builder()
                .productId(0L)
                .price(0.00)
                .id(0L)
                .quantity(25)
                .build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) throws Exception {
        itemService.deleteItem(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> createItem( @Validated(Action.Update.class)@RequestBody ItemDTO itemDTO) throws Exception {

        ItemResponse itemResponse = itemService.createItem(itemDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(itemResponse);
    }
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateItem(@PathVariable("id") Long id,
                                                   @Validated(Action.Create.class) @RequestBody ItemDTO itemDTO) throws Exception {

        itemService.updateItem(id, itemDTO);

        return ResponseEntity.status(204).build();
    }


}
