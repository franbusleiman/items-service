package com.busleiman.items.persistance;

import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.Product;
import com.busleiman.items.domain.entities.Item;
import com.busleiman.items.domain.mappers.ItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    ItemMapper itemMapper = ItemMapper.INSTANCE;

    @Test
    void saveSuccessfully() {

        ItemDTO itemDTO = new ItemDTO(1L, 5);

        Item item = itemMapper.itemDTOToItem(itemDTO);

        item.setProduct(Product.builder()
                .price(55.0)
                .id(1L)
                .createAt(LocalDate.now())
                .name("product name")
                .build());

        itemRepository.save(item);

        List<Item> items = itemRepository.findAll();

        assertEquals(items.size(), 1);
        assertNotNull(items.get(0).getId());
        assertEquals(items.get(0).getPrice(), 5 * 55.0);
        assertEquals(items.get(0).getProduct().getPrice(), 55.0);
    }
}