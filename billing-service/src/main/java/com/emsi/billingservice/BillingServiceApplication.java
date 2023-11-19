package com.emsi.billingservice;

import com.emsi.billingservice.entities.Bill;
import com.emsi.billingservice.entities.ProductItem;
import com.emsi.billingservice.model.Customer;
import com.emsi.billingservice.model.Product;
import com.emsi.billingservice.repositories.BillRepository;
import com.emsi.billingservice.repositories.ProductItemRepository;
import com.emsi.billingservice.service.CustomerRestClient;
import com.emsi.billingservice.service.ProductRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(BillRepository billRepository,
                                   ProductItemRepository productItemRepository,
                                   CustomerRestClient customerRestClient,
                                   ProductRestClient productRestClient){
        return args -> {
            Collection<Product> products = productRestClient.allProducts().getContent();
            Long customerId = 1L;
            Customer customer = customerRestClient.findCustomerById(customerId);
            if(customer == null) throw  new RuntimeException("Customer Not Found");
            Bill bill = Bill.builder().billDate(new Date()).customerId(customerId).build();
            Bill savedBill = billRepository.save(bill);
            products.forEach(p -> {
                ProductItem productItem = ProductItem.builder()
                        .bill(savedBill)
                        .quantity(new Random().nextInt(10))
                        .price(p.getPrice())
                        .productId(p.getId())
                        .discount(Math.random())
                        .build();
                productItemRepository.save(productItem);
            });
        };
    }
}
