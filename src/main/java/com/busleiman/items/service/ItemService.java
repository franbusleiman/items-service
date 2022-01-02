package com.busleiman.items.service;

import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.responses.ItemResponse;
import com.busleiman.items.domain.entities.Item;

import java.util.List;

public interface ItemService {

    List<ItemResponse> findAll();
    List<ItemResponse> findAllSortedByPrice();
    List<ItemResponse> findAllWithHigherPrice(Double price);
    ItemResponse findById(Long id);
    ItemResponse createItem(ItemDTO itemDTO) throws Exception;
    void updateItem(Long id, ItemDTO itemDTO) throws Exception;
    void deleteItem(Long id) throws Exception;

}
