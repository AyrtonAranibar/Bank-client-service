package com.bank.ayrton.client.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


//esta es la entidad que representa a un cliente del banco
@Data
@Document(collection = "clients")
public class Client {
    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String dni;

    private String type; //empresarial o personal
    private ClientSubtype subtype = ClientSubtype.STANDARD; // STANDARD, VIP, PYME

    private Boolean isClient;
}
