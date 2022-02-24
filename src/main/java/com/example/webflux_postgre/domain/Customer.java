package com.example.webflux_postgre.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Customer {
    
    @Id
    private Long id;
    private String firstName;
    private String lastName;
}
