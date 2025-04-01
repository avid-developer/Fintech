package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    // Landing page
    @GetMapping
    public String customersLandingPage(Model model, @RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(defaultValue = "5") int size) {
        Page<Customer> customerPage = customerService.getCustomersPage(PageRequest.of(page, size));
        
        model.addAttribute("customers", customerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("totalItems", customerPage.getTotalElements());
        
        return "customers/landing";
    }
    
    // Customer details screen
    @GetMapping("/{id}")
    public String customerDetails(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        model.addAttribute("customer", customer);
        return "customers/details";
    }
    
    // Form for adding a new customer - starting with name screen
    @GetMapping("/new")
    public String showNameForm(Model model) {
        // Initialize with default values for required fields to pass validation
        Customer customer = new Customer();
        customer.setEmail("placeholder@example.com");
        customer.setPhoneNumber("0000000000");
        customer.setAddress("TBD");
        customer.setCity("TBD");
        customer.setState("TBD");
        customer.setIdProofType("TBD");
        
        model.addAttribute("customer", customer);
        return "customers/name-form";
    }
    
    // Process name form and redirect to ID proof screen
    @PostMapping("/save-name")
    public String saveName(@Valid @ModelAttribute("customer") Customer customer, 
                           BindingResult result, Model model) {
        if (result.hasFieldErrors("name")) {
            return "customers/name-form";
        }
        
        // Ensure required fields have at least placeholder values
        if (customer.getEmail() == null) customer.setEmail("placeholder@example.com");
        if (customer.getPhoneNumber() == null) customer.setPhoneNumber("0000000000");
        if (customer.getAddress() == null) customer.setAddress("TBD");
        if (customer.getCity() == null) customer.setCity("TBD");
        if (customer.getState() == null) customer.setState("TBD");
        if (customer.getIdProofType() == null) customer.setIdProofType("TBD");
        
        // Save the partial customer data
        customer = customerService.saveCustomer(customer);
        
        return "redirect:/customers/" + customer.getId() + "/id-proof";
    }
    
    // ID proof form
    @GetMapping("/{id}/id-proof")
    public String showIdProofForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        model.addAttribute("customer", customer);
        return "customers/id-proof-form";
    }
    
    // Process ID proof form and redirect to contact details
    @PostMapping("/{id}/save-id-proof")
    public String saveIdProof(@PathVariable Long id, 
                             @Valid @ModelAttribute("customer") Customer customer,
                             BindingResult result, Model model) {
        if (result.hasFieldErrors("idProofType") || result.hasFieldErrors("idProofNumber")) {
            return "customers/id-proof-form";
        }
        
        // Get the existing customer
        Customer existingCustomer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        // Update only ID proof fields
        existingCustomer.setIdProofType(customer.getIdProofType());
        existingCustomer.setIdProofNumber(customer.getIdProofNumber());
        
        // Save the updated customer
        customerService.saveCustomer(existingCustomer);
        
        return "redirect:/customers/" + id + "/contact";
    }
    
    // Contact details form
    @GetMapping("/{id}/contact")
    public String showContactForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        model.addAttribute("customer", customer);
        return "customers/contact-form";
    }
    
    // Process contact form and redirect to address form
    @PostMapping("/{id}/save-contact")
    public String saveContact(@PathVariable Long id, 
                             @Valid @ModelAttribute("customer") Customer customer,
                             BindingResult result, Model model) {
        if (result.hasFieldErrors("email") || result.hasFieldErrors("phoneNumber")) {
            return "customers/contact-form";
        }
        
        // Get the existing customer
        Customer existingCustomer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        // Update only contact fields
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        
        // Save the updated customer
        customerService.saveCustomer(existingCustomer);
        
        return "redirect:/customers/" + id + "/address";
    }
    
    // Address form
    @GetMapping("/{id}/address")
    public String showAddressForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        model.addAttribute("customer", customer);
        return "customers/address-form";
    }
    
    // Process address form and redirect to customer details
    @PostMapping("/{id}/save-address")
    public String saveAddress(@PathVariable Long id, 
                             @Valid @ModelAttribute("customer") Customer customer,
                             BindingResult result, Model model) {
        if (result.hasFieldErrors("address") || result.hasFieldErrors("city") || 
            result.hasFieldErrors("state") || result.hasFieldErrors("zipCode")) {
            return "customers/address-form";
        }
        
        // Get the existing customer
        Customer existingCustomer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        // Update only address fields
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setState(customer.getState());
        existingCustomer.setZipCode(customer.getZipCode());
        
        // Save the updated customer
        customerService.saveCustomer(existingCustomer);
        
        return "redirect:/customers/" + id;
    }
    
    // Edit customer details
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        model.addAttribute("customer", customer);
        return "customers/edit-form";
    }
    
    // Process edit form
    @PostMapping("/{id}/update")
    public String updateCustomer(@PathVariable Long id, 
                               @Valid @ModelAttribute("customer") Customer customer,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "customers/edit-form";
        }
        
        customerService.updateCustomer(id, customer);
        return "redirect:/customers/" + id;
    }
    
    // Delete customer
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}