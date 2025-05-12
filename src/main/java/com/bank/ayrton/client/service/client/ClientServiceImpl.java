package com.bank.ayrton.client.service.client;

import com.bank.ayrton.client.api.client.ClientRepository;
import com.bank.ayrton.client.api.client.ClientService;
import com.bank.ayrton.client.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService {

    // se crea una variable que contiene los metodos para usar mongodb llamada repository
    private final ClientRepository repository;

    //indica a Spring que cree una instancia cada vez que ClientServiceImpl lo necesita
    @Autowired
    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Client> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Client> save(Client client) {
        return repository.save(client);
    }

    @Override
    public Mono<Client> update(String id, Client client) {
        //busca el cliente por id
        return repository.findById(id)
                //flatmap devuelve Mono<Client> y permite encadenar llamadas reactivas unas dentro de otras
                .flatMap(existing -> {
                    client.setId(id); // para asegurarte de que se actualiza el correcto
                    //guarda al cliente con el id del objeto viejo
                    return repository.save(client);
                });
    }


    //elimina al usuario, no retorna nada
    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }
}