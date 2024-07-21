package net.enset.ebanking_backend.dtos;

import lombok.Data;
import net.enset.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO {

    private double overDraft;
    private String id;
    private double balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
}
