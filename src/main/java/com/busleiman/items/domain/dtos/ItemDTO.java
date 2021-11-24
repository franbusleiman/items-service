package com.busleiman.items.domain.dtos;


import com.busleiman.items.domain.validationsGroups.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ItemDTO {


    @NotBlank(message = "The field must not be null", groups = {Action.Create.class})
    private Long productId;

    @NotBlank(message = "The field must not be null", groups = {Action.Create.class})
    @Min(value = 1, message = "The quantity must be almost 1", groups = {Action.Create.class,  Action.Update.class})
    private Integer quantity;
}
