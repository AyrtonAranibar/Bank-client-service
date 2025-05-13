package com.bank.ayrton.client.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//el consumer escucha los eventos, recibe del producer en este caso del microservicio movement-service
//la notacion component indica que es una clase getionada por spring
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "movements", groupId = "product-group")
    public void listen(String message) {
        System.out.println("Producto recibió movimiento: " + message);
        // Aquí va la lógica de actualización de saldo, verificación, etc.
    }
}
