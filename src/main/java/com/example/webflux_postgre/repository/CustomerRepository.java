package com.example.webflux_postgre.repository;

import com.example.webflux_postgre.domain.Customer;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    
    @Query("SELECT  * FROM CUSTOMER WHERE LAST_NAME = :lastName")
    Flux<Customer> findByLastName(String lastName);
}
