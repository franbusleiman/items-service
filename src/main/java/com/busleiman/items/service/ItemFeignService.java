package com.busleiman.items.service;

import com.busleiman.items.domain.Item;
import com.busleiman.items.domain.Product;
import com.busleiman.items.service.config.FeignProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemFeignService implements ItemService{

    @Autowired
    private FeignProductClient feignProductClient;


    @Override
    public List<Item> findAll() {

        List<Product> products = feignProductClient.getProducts();

        List<Item> items = products.stream()
                .map(product -> new Item(product, 1))
                .collect(Collectors.toList());

        System.out.println("just passing throw here (feign)");

        return items;
    }

    @Override
    public Item findById(Long id, int quantity) {

        Product product = feignProductClient.getProductById(id);

        Item item = new Item(product, quantity);
        return item;
    }

    @Override
    public Product createProduct(Product product) throws Exception {


        ResponseEntity<Product> productResponseEntity = feignProductClient.createProduct(product);
        if(!productResponseEntity.getStatusCode().is2xxSuccessful()){
            throw new Exception("Error while creating product");
        }

        return productResponseEntity.getBody();
    }

    @Override
    public Product updateProduct(Long id, Product product) throws Exception {

        ResponseEntity<Product> productResponseEntity = feignProductClient.updateProduct(id, product);
        if(!productResponseEntity.getStatusCode().is2xxSuccessful()){
            throw new Exception("Error while updating product");
        }

        return productResponseEntity.getBody();
    }


    @Override
    public void deleteProduct(Long id) {

        feignProductClient.deleteProduct(id);

    }
}
