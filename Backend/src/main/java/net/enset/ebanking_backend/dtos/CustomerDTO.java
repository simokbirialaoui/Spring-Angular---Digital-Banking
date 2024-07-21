package net.enset.ebanking_backend.dtos;

import lombok.Data;
import  jakarta.persistence.* ;


@Data
public class CustomerDTO {

    private Long id;
    private String nom;
    private String  email;

}
