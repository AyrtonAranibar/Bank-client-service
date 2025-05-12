package com.bank.ayrton.client.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


//esta es la entidad que representa a un cliente del banco
@Data
@Document(collection = "clients")
public class Client {
    @Id
    private String id;
    private String name;
    private String dni;
    private String type; //empresarial o personal
}
