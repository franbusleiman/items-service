package com.busleiman.items.domain.entities;

import com.busleiman.items.domain.dtos.Product;
import lombok.*;

import javax.annotation.PostConstruct;
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
    private Product product;

    @Column(name = "quantity")
    @NonNull
    private Integer quantity;

    @Column(name = "price")
    private Double price;


    public void setProduct (Product product){
        this.product = product;
        this.price = product.getPrice() * this.quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
        if(this.product != null){
            this.price = this.product.getPrice() * quantity;
        }
    }
}
