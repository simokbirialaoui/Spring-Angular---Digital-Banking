package net.enset.ebanking_backend;

import net.enset.ebanking_backend.dtos.BankAccountDTO;
import net.enset.ebanking_backend.dtos.CurrentBankAccountDTO;
import net.enset.ebanking_backend.dtos.CustomerDTO;
import net.enset.ebanking_backend.dtos.SanvingBankAccountDTO;
import net.enset.ebanking_backend.entites.*;
import net.enset.ebanking_backend.enums.AccountStatus;
import net.enset.ebanking_backend.enums.OperationType;
import net.enset.ebanking_backend.exceptions.BalanceNotSufficientException;
import net.enset.ebanking_backend.exceptions.BankAccountNotFound;
import net.enset.ebanking_backend.exceptions.CustomerNotFoundException;
import net.enset.ebanking_backend.repositories.AccountOperationRepository;
import net.enset.ebanking_backend.repositories.AccountRepository;
import net.enset.ebanking_backend.repositories.CustomerRepository;
import net.enset.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner (BankAccountService bankAccountService){
        return  args -> {
            Stream.of("Yassmine","Khadija","noura","itimad").forEach(name ->{
                CustomerDTO customer = new CustomerDTO();
                customer.setNom(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());

                    List<BankAccountDTO> accounts = bankAccountService.listBANK_ACCOUNTS();
                    for (BankAccountDTO acc : accounts) {
                        for (int i = 0; i < 10; i++) {
                            String accountId;
                            if(acc instanceof SanvingBankAccountDTO)
                            {
                                accountId =((SanvingBankAccountDTO)acc).getId();

                            }
                            else
                                accountId =((CurrentBankAccountDTO)acc).getId();

                            bankAccountService.credit(accountId, 1000 + Math.random() * 12000, "Credit");
                            bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");



                        }
                    }

                } catch (CustomerNotFoundException | BankAccountNotFound |BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
        };
    }
   // @Bean
    CommandLineRunner start(CustomerRepository customerRepository ,
                            AccountRepository accountRepository,
                            AccountOperationRepository OperationRepository){

        return  args -> {
            Stream.of("Sara","Ali","Noura").forEach(name ->{
                Customer customer = new Customer();
                customer.setNom(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {

                CurrentAccount cur = new CurrentAccount();
                cur.setId(UUID.randomUUID().toString());
                cur.setCreateAt(new Date());
                cur.setBalance(Math.random()*90000);
                cur.setStatus(AccountStatus.CREATED);
                cur.setCustomer(customer);
                cur.setOverDraft(90000);
                accountRepository.save(cur);

                SavingAccount sa = new SavingAccount();
                sa.setId(UUID.randomUUID().toString());
                sa.setCreateAt(new Date());
                sa.setBalance(Math.random()*90000);
                sa.setStatus(AccountStatus.CREATED);
                sa.setCustomer(customer);
                sa.setInterestRate(5.5);
                accountRepository.save(sa);
            });

            accountRepository.findAll().forEach(acc ->{
                for (var i=0;i<10 ;i++)
                {
                    AccountOperation op = new AccountOperation();
                    op.setOperationDate(new Date());
                    op.setType(Math.random()>0.5?OperationType.CREDIT:OperationType.DEBIT);
                    op.setAmount(Math.random()*12000);
                    op.setBankAccount(acc);
                     OperationRepository.save(op);
                }

            });
        };
    }
}

