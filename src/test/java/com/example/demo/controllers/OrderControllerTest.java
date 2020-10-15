package com.example.demo.controllers;

import com.example.demo.TestUtils;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submit_order_happy_path() {
        User u = new User();
        u.setId(1L);
        u.setUsername("test");

        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        u.setCart(cart);

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);

        ResponseEntity<UserOrder> response = orderController.submit(u.getUsername());

        UserOrder order = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", order.getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(799), order.getItems().get(0).getPrice());
        assertEquals(1, order.getItems().size());

    }

    @Test
    public void get_orders_for_user_happy_path() {
        User u = new User();
        u.setId(1L);
        u.setUsername("test");

        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        List<Item> items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        u.setCart(cart);

        UserOrder order = new UserOrder();
        order.setUser(u);
        order.setItems(items);
        order.setTotal(item.getPrice());

        List<UserOrder> orderLst = new ArrayList<>();
        orderLst.add(order);

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        when(orderRepo.findByUser(u)).thenReturn(orderLst);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(u.getUsername());

        List<UserOrder> orders = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", orders.get(0).getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(799), orders.get(0).getItems().get(0).getPrice());
        assertEquals(1, orders.get(0).getItems().size());

    }

}
