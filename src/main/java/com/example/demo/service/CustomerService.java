package com.example.demo.service;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();
    
    Page<Customer> getCustomersPage(Pageable pageable);
    
    Optional<Customer> getCustomerById(Long id);
    
    Customer saveCustomer(Customer customer);
    
    Customer updateCustomer(Long id, Customer customer);
    
    void deleteCustomer(Long id);
}