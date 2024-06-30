package com.example.demo.controllers;

import com.example.demo.FakeData;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCart() {
        Item item = FakeData.getItem1();
        User user = createTestUserWithCart(item);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = createModifyCartRequest();
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(0L, retrievedCart.getId());
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(item, items.get(0));
        assertEquals(new BigDecimal("5.98"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void testAddToCartUserNotFound() {
        Item item = FakeData.getItem1();

        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = createModifyCartRequest();
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCart() {
        Item item = FakeData.getItem1();
        User user = createTestUserWithCart(item);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = createModifyCartRequest();
        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(0L, retrievedCart.getId());
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals(new BigDecimal("0.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    private User createTestUserWithCart(Item item) {
        User user = new User();
        user.setUsername("testUser");

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);

        return user;
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("testUser");
        return request;
    }
}