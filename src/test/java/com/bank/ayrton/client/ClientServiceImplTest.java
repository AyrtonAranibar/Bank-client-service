package com.bank.ayrton.client;

import com.bank.ayrton.client.api.client.ClientRepository;
import com.bank.ayrton.client.entity.Client;
import com.bank.ayrton.client.entity.ClientSubtype;
import com.bank.ayrton.client.service.client.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {

    private ClientRepository repository;
    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ClientRepository.class);
        service = new ClientServiceImpl(repository);
    }

    @Test
    void testFindAll() {
        Client client = new Client();
        client.setId("1");

        when(repository.findAll()).thenReturn(Flux.just(client));

        StepVerifier.create(service.findAll())
                .expectNextMatches(c -> c.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void testFindById() {
        Client client = new Client();
        client.setId("1");

        when(repository.findById("1")).thenReturn(Mono.just(client));

        StepVerifier.create(service.findById("1"))
                .expectNextMatches(c -> c.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void testFindByDni() {
        Client client = new Client();
        client.setDni("12345678");

        when(repository.findByDni("12345678")).thenReturn(Mono.just(client));

        StepVerifier.create(service.findByDni("12345678"))
                .expectNextMatches(c -> c.getDni().equals("12345678"))
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(service.delete("1"))
                .verifyComplete();
    }

    @Test
    void testSaveFailsWhenDniIsNullForPersonal() {
        Client client = new Client();
        client.setType("personal");
        client.setDni(null);

        StepVerifier.create(service.save(client))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testSaveFailsForVipNotPersonal() {
        Client client = new Client();
        client.setType("empresarial");
        client.setSubtype(ClientSubtype.VIP);

        StepVerifier.create(service.save(client))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testSaveFailsForPymeNotEmpresarial() {
        Client client = new Client();
        client.setType("personal");
        client.setSubtype(ClientSubtype.PYME);

        StepVerifier.create(service.save(client))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void testSaveSuccess() {
        Client client = new Client();
        client.setType("personal");
        client.setDni("12345678");
        client.setSubtype(ClientSubtype.VIP);

        when(repository.save(client)).thenReturn(Mono.just(client));

        StepVerifier.create(service.save(client))
                .expectNextMatches(c -> c.getDni().equals("12345678"))
                .verifyComplete();
    }

    @Test
    void testUpdate() {
        Client existing = new Client();
        existing.setId("1");

        Client updated = new Client();
        updated.setName("Nuevo Nombre");

        when(repository.findById("1")).thenReturn(Mono.just(existing));
        when(repository.save(any(Client.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(service.update("1", updated))
                .expectNextMatches(c -> c.getName().equals("Nuevo Nombre"))
                .verifyComplete();
    }

    @Test
    void testSaveFailsForPymeClientNotEmpresarial() {
        Client client = new Client();
        client.setType("personal"); // Tipo incorrecto
        client.setSubtype(ClientSubtype.PYME); // Subtipo PYME requiere tipo empresarial
        client.setDni("84655678");

        StepVerifier.create(service.save(client))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof ResponseStatusException);
                    ResponseStatusException error = (ResponseStatusException) throwable;
                    assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
                    assertTrue(error.getReason().contains("empresariales pueden tener perfil PYME"));
                })
                .verify();
    }
}