package com.busleiman.items.domain.dtos.responses;

import com.busleiman.items.domain.dtos.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ItemResponse extends ItemDTO {

    private Long id;
    private Double price;
}
