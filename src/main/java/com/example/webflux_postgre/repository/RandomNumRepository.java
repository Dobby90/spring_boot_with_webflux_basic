package com.example.webflux_postgre.repository;

import com.example.webflux_postgre.domain.RandomNum;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface RandomNumRepository extends ReactiveCrudRepository<RandomNum, Long> {
    
}
