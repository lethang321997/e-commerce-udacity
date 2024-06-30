package com.example.demo.controllers;

import com.example.demo.FakeData;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetAllItems() {
        List<Item> items = createTestItems();
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(2, retrievedItems.size());
        assertEquals(items.get(0), retrievedItems.get(0));
        assertEquals(items.get(1), retrievedItems.get(1));
    }

    @Test
    public void testGetItemById() {
        Item item = FakeData.getItem2();
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item retrievedItem = response.getBody();
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getDescription(), retrievedItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {
        Item item = FakeData.getItem2();
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(itemRepository.findByName("Round Widget")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> retrievedItems = response.getBody();
        assertNotNull(retrievedItems);
        assertEquals(1, retrievedItems.size());
        assertEquals(item, retrievedItems.get(0));
    }


    private List<Item> createTestItems() {
        Item item1 = FakeData.getItem1();
        Item item2 = FakeData.getItem2();
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        return items;
    }
}