package com.busleiman.items.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "items")
@EqualsAndHashCode
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product")
    @Embedded
    private Product product;

    @Column(name = "quantity")
    private int quantity;

}
