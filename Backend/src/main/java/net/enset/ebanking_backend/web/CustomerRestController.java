package net.enset.ebanking_backend.web;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.enset.ebanking_backend.dtos.BankAccountDTO;
import net.enset.ebanking_backend.dtos.CustomerDTO;
import net.enset.ebanking_backend.entites.Customer;
import net.enset.ebanking_backend.exceptions.BankAccountNotFound;
import net.enset.ebanking_backend.exceptions.CustomerNotFoundException;
import net.enset.ebanking_backend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")

public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public List<CustomerDTO> Customers(){
        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/search")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }
    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public CustomerDTO getCustomerById(@PathVariable (name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public  CustomerDTO saveCustomer(@RequestBody CustomerDTO  customerDTO ){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public  CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO   customerDTO ){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public  void deleteCustomer(@PathVariable Long customerId){
         bankAccountService.deleteCustomer(customerId);
    }
















}
