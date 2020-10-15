package com.example.demo.controllers;

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
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        User u = new User();
        u.setId(1);
        u.setUsername("test");

        Cart cart = new Cart();
        u.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Cart cartResponse = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", cart.getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(799), cart.getItems().get(0).getPrice());

    }

    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        User u = new User();
        u.setId(1);
        u.setUsername("test");

        Cart cart = new Cart();
        u.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Cart cartResponse = response.getBody();

        assertEquals(200, response.getStatusCodeValue());

    }

}
