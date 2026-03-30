package org.example.finansije.converter;

import org.example.finansije.entity.Invoice;
import org.example.finansije.entity.PaymentMethod;
import org.example.finansije.entity.Transaction;
import org.example.finansije.request.TransactionRequest;
import org.example.finansije.response.TransactionResponse;

public class TransactionConverter {
    public static TransactionResponse toResponse(Transaction transaction){
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setInvoiceId(transaction.getInvoice().getId());
        response.setPaymentMethodId(transaction.getPaymentMethod().getId());
        response.setAmount(transaction.getAmount());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public static Transaction toEntity(TransactionRequest request, Invoice invoice, PaymentMethod paymentMethod){
        Transaction transaction = new Transaction();
        transaction.setInvoice(invoice);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(request.getTransactionDate());
        return transaction;
    }
}
