package com.emsi.billingservice.web;

import com.emsi.billingservice.entities.Bill;
import com.emsi.billingservice.repositories.BillRepository;
import com.emsi.billingservice.repositories.ProductItemRepository;
import com.emsi.billingservice.service.CustomerRestClient;
import com.emsi.billingservice.service.ProductRestClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BillRestController {
    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductRestClient productRestClient;

    @GetMapping(path = "fullBill/{id}")
    public Bill bill(@PathVariable Long id){
       Bill bill = billRepository.findById(id).orElseThrow();
       bill.setCustomer(customerRestClient.findCustomerById(bill.getCustomerId()));
       bill.getProductItemList().forEach(p -> p.setProduct(productRestClient.findProductById(p.getId())));
       return bill;
    }
}
