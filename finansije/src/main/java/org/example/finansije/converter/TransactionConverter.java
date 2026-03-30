package org.example.finansije.converter;

import org.example.finansije.entity.Transaction;
import org.example.finansije.response.TransactionResponse;

public class TransactionConverter {
    public static TransactionResponse toResponse(Transaction transaction){
        TransactionResponse response = new TransactionResponse();
        response.setInvoiceId(transaction.getInvoice().getId());
        response.setPaymentMethodId(transaction.getPaymentMethod().getId());
        response.setAmount(transaction.getAmount());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public static Transaction toEntity(TransactionResponse response){
        Transaction transaction = new Transaction();
        transaction.setAmount(response.getAmount());
        transaction.setTransactionDate(response.getTransactionDate());
        return transaction;
    }
}
