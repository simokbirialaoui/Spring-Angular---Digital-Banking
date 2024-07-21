package net.enset.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.enset.ebanking_backend.dtos.*;
import net.enset.ebanking_backend.entites.*;
import net.enset.ebanking_backend.enums.OperationType;
import net.enset.ebanking_backend.exceptions.BalanceNotSufficientException;
import net.enset.ebanking_backend.exceptions.BankAccountNotFound;
import net.enset.ebanking_backend.exceptions.CustomerNotFoundException;
import net.enset.ebanking_backend.mappers.BankAccountMapperImpl;
import net.enset.ebanking_backend.repositories.AccountOperationRepository;
import net.enset.ebanking_backend.repositories.AccountRepository;
import net.enset.ebanking_backend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j    // journalisation
public class BankAccountServiceImpl implements  BankAccountService{
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl dtoMapper;



    // Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("save new customer");
        Customer customer = dtoMapper.fromCustomer(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);;

        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initalBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer mycustomer =customerRepository.findById(customerId).orElse(null);

        CurrentAccount CurrentAccount  = new CurrentAccount();
        CurrentAccount.setId(UUID.randomUUID().toString());
        CurrentAccount.setCreateAt(new Date());
        CurrentAccount.setBalance(initalBalance);
        if(mycustomer ==null)
            throw new CustomerNotFoundException("Customer not fund");

        CurrentAccount.setCustomer(mycustomer);
        CurrentAccount.setOverDraft(overDraft);

        CurrentAccount currentAccount = accountRepository.save(CurrentAccount);

        return dtoMapper.fromCurrentBankAccount(currentAccount);
    }

    @Override
    public SanvingBankAccountDTO saveSavingBankAccount(double initalBalance, double interesRate, Long customerId) throws CustomerNotFoundException {
        Customer mycustomer =customerRepository.findById(customerId).orElse(null);

        SavingAccount SavingAccount  = new SavingAccount();

        SavingAccount.setId(UUID.randomUUID().toString());
        SavingAccount.setCreateAt(new Date());
        SavingAccount.setBalance(initalBalance);
        if(mycustomer ==null)
            throw new CustomerNotFoundException("Customer not fund");

        SavingAccount.setCustomer(mycustomer);
        SavingAccount.setInterestRate(interesRate);

        SavingAccount savingAccount = accountRepository.save(SavingAccount);

        return dtoMapper.fromSavingBankAccount(savingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> Customers= customerRepository.findAll();
        List<CustomerDTO> lsCustomerDTO= Customers.stream()
                .map(cust ->dtoMapper.fromCustomer(cust))
                .collect(Collectors.toList());
       return lsCustomerDTO;
    }

    @Override
    public BankAccountDTO getBankAccount(String accuntId) throws BankAccountNotFound {
        BankAccount bankAccount = accountRepository.findById(accuntId)
                .orElseThrow(()->new BankAccountNotFound("BankAccount not found"));
        if(bankAccount instanceof SavingAccount)
        {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return  dtoMapper.fromSavingBankAccount(savingAccount);
        }
        else
        {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return  dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accuntId, double amount, String decription) throws BalanceNotSufficientException, BankAccountNotFound {

        BankAccount bankAccount = accountRepository.findById(accuntId)
                .orElseThrow(()->new BankAccountNotFound("BankAccount not found"));

        if(bankAccount.getBalance()<amount)
            throw  new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation  = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(decription);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance()-amount);

        accountRepository.save(bankAccount);



    }

    @Override
    public void credit(String accuntId, double amount, String decription) throws BankAccountNotFound {

        BankAccount bankAccount = accountRepository.findById(accuntId)
                .orElseThrow(()->new BankAccountNotFound("BankAccount not found"));


        AccountOperation accountOperation  = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(decription);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance()+amount);

        accountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accuntIdSource, String accuntIdDestination, double amount) throws BankAccountNotFound, BalanceNotSufficientException {

        debit(accuntIdSource ,amount,"Transfer to"+accuntIdDestination);
        credit(accuntIdDestination ,amount,"Transfer from"+accuntIdSource);

    }

    @Override
    public List<BankAccountDTO> listBANK_ACCOUNTS(){
        List<BankAccount> bankAccounts = accountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("save new customer");
        Customer customer = dtoMapper.fromCustomer(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);;

        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public void deleteCustomer(Long IdcustomerDTO) {
        customerRepository.deleteById(IdcustomerDTO);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFound {
        BankAccount bankAccount=accountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFound("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> getBankAccountByCustomer(Customer customer) {

        List<BankAccount> bankAccounts = accountRepository.findByCustomer(customer);

        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;

    }


}
