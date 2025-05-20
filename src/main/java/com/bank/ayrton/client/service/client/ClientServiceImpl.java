package com.bank.ayrton.client.service.client;

import com.bank.ayrton.client.api.client.ClientRepository;
import com.bank.ayrton.client.api.client.ClientService;
import com.bank.ayrton.client.entity.Client;
import com.bank.ayrton.client.entity.ClientSubtype;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
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
        log.info("Obteniendo todos los clientes");
        return repository.findAll();
    }

    @Override
    public Mono<Client> findById(String id) {
        log.info("Buscando cliente con ID: {}", id);
        return repository.findById(id);
    }

    @Override
    public Mono<Client> save(Client client) {
        log.info("Guardando nuevo cliente: {}", client);

        // el cliente personal no puede ser null ni vacio
        if ("personal".equalsIgnoreCase(client.getType()) && (client.getDni() == null || client.getDni().isBlank())) {
            String errorMessage = "El cliente personal debe tener un DNI válido";
            log.error("Error esperado: {}", errorMessage);
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
        }

        //validacion cuenta personal con subtipo VIP
        if (client.getSubtype() == ClientSubtype.VIP && !"personal".equalsIgnoreCase(client.getType())) {
            String errorMessage = "Solo los clientes personales pueden tener perfil VIP";
            log.error("Error esperado: {}", errorMessage);
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
        }

        //validacion cuenta empresarial con subtipo PYME
        if (client.getSubtype() == ClientSubtype.PYME && !"empresarial".equalsIgnoreCase(client.getType())) {
            String errorMessage = "Solo los clientes empresariales pueden tener perfil PYME";
            log.error("Error esperado: {}", errorMessage);
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));
        }

        return repository.save(client);
    }

    @Override
    public Mono<Client> update(String id, Client client) {
        log.info("Actualizando cliente con ID: {}", id);
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
        log.info("Eliminando cliente con ID: {}", id);
        return repository.deleteById(id);
    }

    @Override
    public Mono<Client> findByDni(String dni) {
        log.info("Buscando cliente con DNI: {}", dni);
        return repository.findByDni(dni);
    }
}