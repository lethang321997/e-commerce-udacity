package com.example.demo;

import com.example.demo.model.persistence.Item;

import java.math.BigDecimal;

public class FakeData {
    public static Item getItem1() {
        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("A widget that is round");
        return item;
    }

    public static Item getItem2() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setPrice(new BigDecimal("1.99"));
        item.setDescription("A widget that is square");
        return item;
    }
}
