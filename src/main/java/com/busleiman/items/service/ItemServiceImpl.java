package com.busleiman.items.service;


import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.Product;
import com.busleiman.items.domain.dtos.responses.ItemResponse;
import com.busleiman.items.domain.entities.Item;
import com.busleiman.items.domain.mappers.ItemMapper;
import com.busleiman.items.exceptions.NotFoundException;
import com.busleiman.items.persistance.ItemRepository;
import com.busleiman.items.service.config.FeignProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private FeignProductClient feignProductClient;

    @Autowired
    private ItemRepository itemRepository;

    private ItemMapper itemMapper = ItemMapper.INSTANCE;


    @Override
    public List<ItemResponse> findAll() {

        return itemRepository.findAll().stream()
                .map(item -> itemMapper.itemToItemResponse(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> findAllWithHigherPrice(Double price) {


        return itemRepository.findAll().stream()
                .filter(item -> item.getPrice() > price)
                .map(item -> itemMapper.itemToItemResponse(item))
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse findById(Long id) {

        return itemMapper.itemToItemResponse(itemRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @Override
    public ItemResponse createItem(ItemDTO itemDTO) throws Exception {

        ResponseEntity<Product> productResponseEntity = feignProductClient.getProductById(itemDTO.getProductId());
        if (!productResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error while finding product");
        }

        Item item = itemMapper.itemDTOToItem(itemDTO);

        item.setProduct(productResponseEntity.getBody());

        itemRepository.save(item);

        return itemMapper.itemToItemResponse(item);
    }

    @Override
    public void updateItem(Long id, ItemDTO itemDTO) throws Exception {

        Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);


        if(itemDTO.getProductId() != null && itemDTO.getProductId() != item.getProduct().getId() ) {

            ResponseEntity<Product> productResponseEntity = feignProductClient.getProductById(itemDTO.getProductId());
            if (!productResponseEntity.getStatusCode().is2xxSuccessful()) {
                throw new Exception("Error while finding product");
            }

            item.setProduct(productResponseEntity.getBody());
        }

        if(itemDTO.getQuantity() != null){
        item.setQuantity(itemDTO.getQuantity());
        }

        itemRepository.save(item);
    }


    @Override
    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        itemRepository.delete(item);

    }
}
