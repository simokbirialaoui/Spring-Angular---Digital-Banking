package net.enset.ebanking_backend.services;

import net.enset.ebanking_backend.dtos.*;
import net.enset.ebanking_backend.entites.BankAccount;
import net.enset.ebanking_backend.entites.CurrentAccount;
import net.enset.ebanking_backend.entites.Customer;
import net.enset.ebanking_backend.entites.SavingAccount;
import net.enset.ebanking_backend.exceptions.BalanceNotSufficientException;
import net.enset.ebanking_backend.exceptions.BankAccountNotFound;
import net.enset.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    // Logger log = LoggerFactory.getLogger(this.getClass().getName());
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount (double initalBalance , double overDraft , Long customerId) throws CustomerNotFoundException;
    SanvingBankAccountDTO saveSavingBankAccount (double initalBalance , double interesRate , Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accuntId) throws BankAccountNotFound;
    void debit (String accuntId ,double amount ,String decription) throws BankAccountNotFound, BalanceNotSufficientException;
    void credit (String accuntId ,double amount ,String decription) throws BankAccountNotFound, BalanceNotSufficientException;
    void transfer (String accuntIdSource ,String accuntIdDestination ,double amount ) throws BankAccountNotFound, BalanceNotSufficientException;

    List<BankAccountDTO> listBANK_ACCOUNTS();


    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long IdcustomerDTO);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFound;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccountDTO> getBankAccountByCustomer( Customer customerId);
}
