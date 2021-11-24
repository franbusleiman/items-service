package com.busleiman.items.domain.dtos;

import lombok.*;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Product implements Serializable {

    private Long id;

    private String name;

    private LocalDate createAt;

    private Double price;

}
