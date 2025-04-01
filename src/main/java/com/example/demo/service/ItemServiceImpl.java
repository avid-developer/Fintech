package com.example.demo.service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    
    @Override
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    
    @Override
    public Item updateItem(Long id, Item itemDetails) {
        return itemRepository.findById(id)
            .map(item -> {
                item.setName(itemDetails.getName());
                item.setDescription(itemDetails.getDescription());
                return itemRepository.save(item);
            })
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }
    
    @Override
    public void deleteItem(Long id) {
        itemRepository.findById(id).ifPresentOrElse(
            itemRepository::delete,
            () -> {
                throw new RuntimeException("Item not found with id: " + id);
            }
        );
    }
}