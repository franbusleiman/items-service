package com.busleiman.items.domain.mappers;

import com.busleiman.items.domain.dtos.ItemDTO;
import com.busleiman.items.domain.dtos.responses.ItemResponse;
import com.busleiman.items.domain.entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "productId", source = "product.id")
    ItemResponse itemToItemResponse(Item item);

    @Mapping(target = "id", ignore = true)
    Item itemDTOToItem(ItemDTO itemDTO);

}
