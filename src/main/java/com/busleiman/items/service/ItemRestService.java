package com.busleiman.items.service;

import com.busleiman.items.domain.Item;
import com.busleiman.items.domain.Product;
import com.busleiman.items.persistance.ItemRepository;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class ItemRestService implements ItemService{


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Item> findAll() {

        List<Product> products = Arrays.asList(restTemplate.getForObject("http://products-service/products/", Product[].class));

        List<Item> items = products.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());

        System.out.println("just passing throw here (restTemplate)");


        return items;
    }

    @Override
    public Item findById(Long id, int quantity) {

        Map<String, String> properties = new HashMap<>();
        properties.put("id", id.toString());

        Product product = restTemplate.getForObject("http://products-service/products/{id}", Product.class, properties);

        Item item = new Item(product, quantity);
        return item;
    }

    @Override
    public Product createProduct(Product product) throws Exception {
        HttpEntity<Product> httpEntity = new HttpEntity(product);

        ResponseEntity responseEntity = restTemplate.postForEntity("http://products-service/products", httpEntity, Product.class);

        if(! responseEntity.getStatusCode().is2xxSuccessful()){
            throw new Exception("Error while creating");
        }
        Product product1 = (Product) responseEntity.getBody();

        return product1;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws Exception {
        HttpEntity<Product> httpEntity = new HttpEntity(product);

        Map<String, String> properties = new HashMap<>();
        properties.put("id", id.toString());

        ResponseEntity responseEntity = restTemplate.exchange("http://products-service/products/{id}", HttpMethod.PUT,  httpEntity, Product.class, properties);

        if(! responseEntity.getStatusCode().is2xxSuccessful()){
            throw new Exception("Error while creating");
        }
        Product product1 = (Product) responseEntity.getBody();

        return product1;
    }

    @Override
    public void deleteProduct(Long id) throws Exception {

        Map<String, String> properties = new HashMap<>();
        properties.put("id", id.toString());

        restTemplate.delete("http://products-service/products/{id}", properties);
    }
}
