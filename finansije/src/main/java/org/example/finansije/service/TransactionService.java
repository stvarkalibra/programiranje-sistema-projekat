package org.example.finansije.service;

import lombok.RequiredArgsConstructor;
import org.example.finansije.converter.TransactionConverter;
import org.example.finansije.entity.Invoice;
import org.example.finansije.entity.PaymentMethod;
import org.example.finansije.entity.Transaction;
import org.example.finansije.repository.InvoiceRepository;
import org.example.finansije.repository.PaymentMethodRepository;
import org.example.finansije.repository.TransactionRepository;
import org.example.finansije.request.TransactionRequest;
import org.example.finansije.response.PaymentMethodStatsResponse;
import org.example.finansije.response.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public TransactionResponse save(TransactionRequest request) {

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        Transaction transaction = TransactionConverter.toEntity(request, invoice, paymentMethod);


        Transaction saved = transactionRepository.save(transaction);

        return TransactionConverter.toResponse(saved);
    }

    public List<TransactionResponse> findAll() {

        return transactionRepository.findAll()
                .stream()
                .map(TransactionConverter::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse findById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return TransactionConverter.toResponse(transaction);
    }

    public List<TransactionResponse> findByInvoiceId(Long invoiceId) {

        return transactionRepository.findByInvoice_Id(invoiceId)
                .stream()
                .map(TransactionConverter::toResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> findByPaymentMethodId(Long paymentMethodId) {

        return transactionRepository.findByPaymentMethod_Id(paymentMethodId)
                .stream()
                .map(TransactionConverter::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse update(Long id, TransactionRequest request) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        transaction.setInvoice(invoice);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction updated = transactionRepository.save(transaction);

        return TransactionConverter.toResponse(updated);
    }

    public void deleteById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public List<PaymentMethodStatsResponse> getPaymentMethodStatistics() {
        return transactionRepository.getPaymentMethodStatistics();
    }
}