package com.example.webflux_postgre.controller;

import java.time.Duration;

import com.example.webflux_postgre.domain.Customer;
import com.example.webflux_postgre.domain.RandomNum;
import com.example.webflux_postgre.repository.CustomerRepository;
import com.example.webflux_postgre.repository.RandomNumRepository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    
    private final CustomerRepository customerRepository;
    private final RandomNumRepository randomNumRepository;

    @GetMapping(value = "/list")
    public Flux<Customer> callCustomer() {
        log.info("========== Call Customer List ==========");
        return customerRepository.findAll();
    }

    @GetMapping(value = "/listsse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> callCustomerSSE() {
        log.info("========== Call Customer List SSE ==========");
        return customerRepository.findAll();
    }

    /**
     * SSE NOT USE
     * @return
     */
    @GetMapping(value = "/random")
    public Flux<RandomNum> callRandomNum() {
        log.info("========== Call Random Number List ==========");
        return randomNumRepository.findAll();
    }

    /**
     * SSE USE
     * @return
     */
    @GetMapping(value = "/randomsse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<RandomNum> callRandomNumSSE() {
        log.info("========== Call Random Number List SSE ==========");
        return randomNumRepository.findAll();
    }

    @GetMapping(value = "/flux")
    public Flux<Integer> callFluxTest() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
    }

    // APPLICATION_STREAM_JSON_VALUE, APPLICATION_NDJSON_VALUE, TEXT_EVENT_STREAM_VALUE
    @GetMapping(value = "/fluxsse", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> callFluxTestSSE() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
    }

}
