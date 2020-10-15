package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void get_all_items_happy_path() {
        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        List<Item> itemLst = new ArrayList<>();
        itemLst.add(item);

        when(itemRepo.findAll()).thenReturn(itemLst);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        List<Item> items = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", items.get(0).getName());
        assertEquals("brand new iPhone 12", items.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(799), items.get(0).getPrice());
        assertEquals(1, items.size());

    }

    @Test
    public void get_item_by_id_happy_path() {
        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        Item itemResponse = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", itemResponse.getName());
        assertEquals("brand new iPhone 12", itemResponse.getDescription());
        assertEquals(BigDecimal.valueOf(799), itemResponse.getPrice());

    }

    @Test
    public void get_item_by_name_happy_path() {
        Item item = new Item();
        item.setId(1L);
        item.setName("iPhone 12");
        item.setDescription("brand new iPhone 12");
        item.setPrice(BigDecimal.valueOf(799));

        List<Item> itemLst = new ArrayList<>();
        itemLst.add(item);

        when(itemRepo.findByName("iPhone 12")).thenReturn(itemLst);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("iPhone 12");

        List<Item> items = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("iPhone 12", items.get(0).getName());
        assertEquals("brand new iPhone 12", items.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(799), items.get(0).getPrice());
        assertEquals(1, items.size());

    }

}
