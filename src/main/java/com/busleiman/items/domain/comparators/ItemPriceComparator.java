package com.busleiman.items.domain.comparators;

import com.busleiman.items.domain.entities.Item;

import java.util.Comparator;

public class ItemPriceComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        return (int) (o1.getPrice() - o2.getPrice());
    }
}
