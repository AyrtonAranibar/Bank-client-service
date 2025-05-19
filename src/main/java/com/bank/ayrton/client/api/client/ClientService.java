package com.bank.ayrton.client.api.client;

import com.bank.ayrton.client.entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Flux<Client> findAll();
    Mono<Client> findById(String id);
    Mono<Client> save(Client client);
    Mono<Client> update(String id, Client client);
    Mono<Void> delete(String id);
    Mono<Client> findByDni(String dni);
}
