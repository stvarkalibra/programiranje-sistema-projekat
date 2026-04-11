package org.example.rezervacije.converters;

import org.example.rezervacije.entity.CancellationPolicy;
import org.example.rezervacije.request.CancellationPolicyRequest;
import org.example.rezervacije.response.CancellationPolicyResponse;

public class CancellationPolicyConverter {
    public static CancellationPolicyResponse toResponse(CancellationPolicy cancellationPolicy) {
        CancellationPolicyResponse response = new CancellationPolicyResponse();
        response.setId(cancellationPolicy.getId());
        response.setPolicyName(cancellationPolicy.getPolicyName());
        response.setRefundPercentage(cancellationPolicy.getRefundPercentage());
        return response;
    }

    public static CancellationPolicy toEntity(CancellationPolicyRequest request) {
        CancellationPolicy cancellationPolicy = new CancellationPolicy();
        cancellationPolicy.setPolicyName(request.getPolicyName());
        cancellationPolicy.setRefundPercentage(request.getRefundPercentage());
        return cancellationPolicy;
    }
}
