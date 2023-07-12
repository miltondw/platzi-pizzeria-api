package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.CustomerEntity;
import com.platzi.pizza.persistence.entity.OrderEntity;
import com.platzi.pizza.persistence.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    @Autowired
    public CustomerService(CustomerRepository customerRepository, OrderService orderService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }
    @GetMapping("/phone/{phone}")
    public CustomerEntity findByPhone(String phone){
        return customerRepository.findByPhone(phone);
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderEntity>> getCustomerOrders(@PathVariable String id) {
        return ResponseEntity.ok(this.orderService.getCustomerOrders(id));
    }
}
