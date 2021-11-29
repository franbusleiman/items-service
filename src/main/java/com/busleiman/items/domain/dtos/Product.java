package com.busleiman.items.domain.dtos;

import lombok.*;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Product implements Serializable {

    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "product_create_at")
    private LocalDate createAt;
    @Column(name = "product_price")
    private Double price;
}
