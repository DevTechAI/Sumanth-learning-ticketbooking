package com.example.batch.processor;

import com.example.batch.model.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) {
        customer.setName(customer.getName().toUpperCase());
        customer.setEmail(customer.getEmail().toLowerCase());
        System.out.println("PROCESSING: " + customer);
        return customer;
    }
}
