package com.emsi.billingservice.model;

import jakarta.persistence.ElementCollection;
import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String name;
    private String email;
}
