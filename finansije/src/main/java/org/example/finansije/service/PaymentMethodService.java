package org.example.finansije.service;

import lombok.RequiredArgsConstructor;
import org.example.finansije.converter.PaymentMethodConverter;
import org.example.finansije.entity.PaymentMethod;
import org.example.finansije.repository.PaymentMethodRepository;
import org.example.finansije.request.PaymentMethodRequest;
import org.example.finansije.response.PaymentMethodResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodResponse save(PaymentMethodRequest request) {

        if (paymentMethodRepository.existsByMethodName(request.getMethodName())) {
            throw new RuntimeException("Payment method already exists");
        }

        PaymentMethod paymentMethod = PaymentMethodConverter.toEntity(request);

        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);

        return PaymentMethodConverter.toResponse(saved);
    }

    public List<PaymentMethodResponse> findAll() {

        return paymentMethodRepository.findAll()
                .stream()
                .map(PaymentMethodConverter::toResponse)
                .collect(Collectors.toList());
    }

    public PaymentMethodResponse findById(Long id) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        return PaymentMethodConverter.toResponse(paymentMethod);
    }

    public List<PaymentMethodResponse> findByActive(boolean isActive) {

        return paymentMethodRepository.findByIsActive(isActive)
                .stream()
                .map(PaymentMethodConverter::toResponse)
                .collect(Collectors.toList());
    }

    public PaymentMethodResponse update(Long id, PaymentMethodRequest request) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        paymentMethod.setMethodName(request.getMethodName());
        paymentMethod.setActive(request.isActive());

        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);

        return PaymentMethodConverter.toResponse(updated);
    }

    public void deleteById(Long id) {

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        paymentMethodRepository.delete(paymentMethod);
    }
}