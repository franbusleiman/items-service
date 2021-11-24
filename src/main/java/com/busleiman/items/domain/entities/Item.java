package com.busleiman.items.domain.entities;

import com.busleiman.items.domain.dtos.Product;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "items")
@EqualsAndHashCode
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product")
    @Embedded
    @NonNull
    private Product product;

    @Column(name = "quantity")
    @NonNull
    private Integer quantity;

    @Column(name = "price")
    private Double price = product.getPrice() * quantity;

}
