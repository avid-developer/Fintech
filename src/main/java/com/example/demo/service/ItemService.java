package com.example.demo.service;

import com.example.demo.entity.Item;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAllItems();
    Optional<Item> getItemById(Long id);
    Item createItem(Item item);
    Item updateItem(Long id, Item itemDetails);
    void deleteItem(Long id);
}