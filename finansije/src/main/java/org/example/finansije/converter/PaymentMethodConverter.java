package org.example.finansije.converter;

import org.example.finansije.entity.PaymentMethod;
import org.example.finansije.request.PaymentMethodRequest;
import org.example.finansije.response.PaymentMethodResponse;

public class PaymentMethodConverter {
    public static PaymentMethodResponse toResponse(PaymentMethod paymentMethod) {
        PaymentMethodResponse response = new PaymentMethodResponse();
        response.setId(paymentMethod.getId());
        response.setMethodName(paymentMethod.getMethodName());
        response.setActive(paymentMethod.isActive());
        return response;
    }
    public static PaymentMethod toEntity(PaymentMethodRequest request) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(request.getMethodName());
        paymentMethod.setActive(request.isActive());
        return paymentMethod;
    }
}
