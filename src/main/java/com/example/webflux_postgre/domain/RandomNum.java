package com.example.webflux_postgre.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RandomNum {
    
    @Id
    private Long id;
    private Float num; 
}
