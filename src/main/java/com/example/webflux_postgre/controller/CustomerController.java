package com.example.webflux_postgre.controller;

import java.time.Duration;

import com.example.webflux_postgre.domain.Customer;
import com.example.webflux_postgre.domain.RandomNum;
import com.example.webflux_postgre.repository.CustomerRepository;
import com.example.webflux_postgre.repository.RandomNumRepository;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
// @RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    
    private final CustomerRepository customerRepository;
    private final RandomNumRepository randomNumRepository;
    private final Sinks.Many<Customer> sink;

    // A request -> Flux -> Stream
    // B request -> Flux -> Stream
    // -> Flux.merger -> sink : 각 요청마다 데이터가 추가되어 다른 데이터를 받을 수 있는데 그 부분을 싱크를 맞출 수 있다(?)

    public CustomerController(CustomerRepository customerRepository, RandomNumRepository randomNumRepository) {
        this.customerRepository = customerRepository;
        this.randomNumRepository = randomNumRepository;
        sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping(value = "/list")
    public Flux<Customer> callCustomer() {
        log.info("========== Call Customer List ==========");
        return customerRepository.findAll();
    }

    // produces = MediaType.TEXT_EVENT_STREAM_VALUE 생략 가능
    @GetMapping(value = "/listsse")
    public Flux<ServerSentEvent<Customer>> callCustomerSSE() {
        log.info("========== Call Customer List SSE SINK ==========");
        return sink.asFlux().map((c) -> ServerSentEvent.builder(c).build()).doOnCancel(() -> {
            sink.asFlux().blockLast();
        });
    }
 
    @PostMapping(value = "/save")
    public Mono<Customer> save() {
        return customerRepository.save(new Customer("do", "bby")).doOnNext((c) -> {
            sink.tryEmitNext(c);
        });
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
    @GetMapping(value = "/fluxsse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> callFluxTestSSE() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
    }

}
