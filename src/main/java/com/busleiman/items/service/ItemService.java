package com.busleiman.items.service;

import com.busleiman.items.domain.Item;
import com.busleiman.items.domain.Product;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long id, int quantity);
    Product createProduct(Product product) throws Exception;
    Product updateProduct(Long id, Product product) throws Exception;
    void deleteProduct(Long id) throws Exception;

}
