package net.enset.ebanking_backend.exceptions;

public class BankAccountNotFound extends Exception {
    public BankAccountNotFound(String message) {
        super(message);
    }
}
