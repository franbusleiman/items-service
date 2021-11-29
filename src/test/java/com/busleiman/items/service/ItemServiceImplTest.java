package com.busleiman.items.service;

import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.Product;
import com.busleiman.items.domain.dtos.responses.ItemResponse;
import com.busleiman.items.domain.entities.Item;
import com.busleiman.items.exceptions.NotFoundException;
import com.busleiman.items.persistance.ItemRepository;
import com.busleiman.items.service.config.FeignProductClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    FeignProductClient feignProductClient;

    @InjectMocks
    ItemServiceImpl itemService;


    private static final Integer QUANTITY = 5;
    private static final Long ID = 1L;
    private static final Integer QUANTITY2 = 15;
    private static final Long ID2 = 2L;
    private static final Integer QUANTITY3 = 9;
    private static final Long ID3 = 3L;
    private static final Long PRODUCT_ID = 23L;
    private static final Long PRODUCT_ID2 = 2L;
    private static final String PRODUCT_NAME = "Product name";
    private static final Double PRODUCT_PRICE = 1000.98;
    private static final Double PRODUCT_PRICE2 = 2000.98;
    private static final LocalDate PRODUCT_DATE = LocalDate.of(2020, 9, 10);

    @Test
    void findAll() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(getItem(QUANTITY, ID)));

        List<ItemResponse> itemList = itemService.findAll();

        verify(itemRepository, times(1)).findAll();
        assertEquals(1, itemList.size());
        assertEquals(ID, itemList.get(0).getId());
        assertEquals(PRODUCT_ID, itemList.get(0).getProductId());
        assertEquals(QUANTITY * PRODUCT_PRICE , itemList.get(0).getPrice());
    }

    @Test
    void findAllWithHigherPrice() {
        Item item1 = getItem(QUANTITY, ID);
        Item item2 = getItem(QUANTITY2, ID2);
        Item item3 = getItem(QUANTITY3, ID3);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2, item3));

        List<ItemResponse> itemResponses = itemService.findAllWithHigherPrice(QUANTITY * PRODUCT_PRICE);

        verify(itemRepository, times(1)).findAll();

        assertEquals(2, itemResponses.size());
        assertEquals(ID2, itemResponses.get(0).getId());
        assertEquals(ID3, itemResponses.get(1).getId());
    }

    @Test
    void findById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem(QUANTITY, ID)));

        ItemResponse itemResponse = itemService.findById(1L);

        verify(itemRepository, times(1)).findById(anyLong());

        assertEquals(ID, itemResponse.getId());
        assertEquals(QUANTITY, itemResponse.getQuantity());
        assertEquals(PRODUCT_ID, itemResponse.getProductId());
        assertEquals(QUANTITY * PRODUCT_PRICE, itemResponse.getPrice());
    }

    @Test
    void createItem() throws Exception {
        when(feignProductClient.getProductById(anyLong())).thenReturn(ResponseEntity.ok(getProduct(PRODUCT_ID, PRODUCT_PRICE)));

        ItemResponse itemResponse = itemService.createItem(getItemDTO(PRODUCT_ID, QUANTITY));

        verify(itemRepository, times(1)).save(any());
        verify(feignProductClient, times(1)).getProductById(anyLong());


        assertEquals(QUANTITY * PRODUCT_PRICE, itemResponse.getPrice());
        assertEquals(PRODUCT_ID, itemResponse.getProductId());
    }

    @Test
    void updateItemQuantity() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem(QUANTITY, ID)));

        ItemDTO itemDTO = getItemDTO(PRODUCT_ID, QUANTITY2);

        ArgumentCaptor<Item> argumentCaptor = ArgumentCaptor.forClass(Item.class);

        itemService.updateItem(ID, itemDTO);

        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(argumentCaptor.capture());
        verify(feignProductClient, times(0)).getProductById(anyLong());


        Item item = argumentCaptor.getValue();

        assertEquals(ID, item.getId());
        assertEquals(PRODUCT_ID, item.getProduct().getId());
        assertEquals(QUANTITY2, item.getQuantity());
        assertEquals(QUANTITY2 * item.getProduct().getPrice(), item.getPrice());
    }

    @Test
    void updateItemProduct() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem(QUANTITY, ID)));
        when(feignProductClient.getProductById(anyLong())).thenReturn(ResponseEntity.ok(getProduct(PRODUCT_ID2, PRODUCT_PRICE2)));

        ItemDTO itemDTO = getItemDTO(PRODUCT_ID2, QUANTITY);

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);

        itemService.updateItem(ID, itemDTO);

        verify(itemRepository, times(1)).findById(anyLong());
        verify(feignProductClient, times(1)).getProductById(anyLong());
        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());


        Item item = itemArgumentCaptor.getValue();

        assertEquals(ID, item.getId());
        assertEquals(PRODUCT_ID2, item.getProduct().getId());
        assertEquals(QUANTITY, item.getQuantity());
        assertEquals(QUANTITY * PRODUCT_PRICE2, item.getPrice());
    }

    @Test
    void updateItemQuantityAndProduct() throws Exception {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem(QUANTITY, ID)));
        when(feignProductClient.getProductById(anyLong())).thenReturn(ResponseEntity.ok(getProduct(PRODUCT_ID2, PRODUCT_PRICE2)));

        ItemDTO itemDTO = getItemDTO(PRODUCT_ID2, QUANTITY2);

        ArgumentCaptor<Item> itemArgumentCaptor = ArgumentCaptor.forClass(Item.class);

        itemService.updateItem(ID, itemDTO);

        verify(itemRepository, times(1)).findById(anyLong());
        verify(feignProductClient, times(1)).getProductById(anyLong());
        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());


        Item item = itemArgumentCaptor.getValue();

        assertEquals(ID, item.getId());
        assertEquals(PRODUCT_ID2, item.getProduct().getId());
        assertEquals(QUANTITY2, item.getQuantity());
        assertEquals(QUANTITY2 * PRODUCT_PRICE2, item.getPrice());
    }

    @Test
    void deleteItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem(QUANTITY, ID)));

        itemService.deleteItem(1L);
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).delete(any());

    }

    @Test
    void productNotFound() {
        when(feignProductClient.getProductById(anyLong())).thenReturn(ResponseEntity.badRequest().build());

        ItemDTO itemDTO = getItemDTO(PRODUCT_ID, QUANTITY);

        assertThrows(Exception.class, () -> itemService.createItem(itemDTO));
    }


    @Test
    void findByIdNotFound() {
        when(
                itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.findById(1L));
    }

    private Item getItem(int quantity, Long id){
        Item item = new Item(quantity);

        item.setProduct(Product.builder()
                .createAt(PRODUCT_DATE)
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build());

        item.setId(id);

        return item;
    }

    private ItemDTO getItemDTO(Long productId, int quantity){
        return ItemDTO.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    private Product getProduct(Long id, Double price){
        return Product.builder()
                .createAt(PRODUCT_DATE)
                .id(id)
                .name(PRODUCT_NAME)
                .price(price)
                .build();
    }
}