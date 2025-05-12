package com.bank.ayrton.client.api.client;

import com.bank.ayrton.client.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


// al heredar de ReactiveMongoRepository heredamos tambien metodos predefinidos para usar mongodb
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
    //aqui se pueden agregar metodos personalizados
}
