package net.enset.ebanking_backend.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import net.enset.ebanking_backend.entites.Customer;
import net.enset.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
public class SanvingBankAccountDTO extends  BankAccountDTO {
    private double interestRate;
    private String id;
    private double balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
}
