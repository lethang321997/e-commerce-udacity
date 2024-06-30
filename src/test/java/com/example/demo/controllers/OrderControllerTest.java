package com.example.demo.controllers;

import com.example.demo.FakeData;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        com.example.demo.TestUtils.injectObjects(orderController, "userRepository", userRepository);
        com.example.demo.TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmitOrder() {
        String username = "testUser";
        User user = createUser(username);
        Cart cart = createCart(user);
        user.setCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertNotNull(userOrder.getItems());
        assertNotNull(userOrder.getTotal());
        assertNotNull(userOrder.getUser());
    }

    @Test
    public void testSubmitOrderWithNullUser() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForExistingUser() {
        String username = "testUser";
        User user = createUser(username);
        Cart cart = createCart(user);
        UserOrder order = UserOrder.createFromCart(cart);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
        assertEquals(1, userOrders.size());
    }

    @Test
    public void testGetOrdersForNonExistentUser() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(0L);
        return user;
    }

    private Cart createCart(User user) {
        Item item = FakeData.getItem1();
        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal("2.99"));
        cart.setUser(user);
        user.setCart(cart);
        return cart;
    }
}