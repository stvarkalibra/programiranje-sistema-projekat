package org.example.rezervacije.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.CancellationPolicyConverter;
import org.example.rezervacije.entity.CancellationPolicy;
import org.example.rezervacije.repository.BookingRepository;
import org.example.rezervacije.repository.CancellationPolicyRepository;
import org.example.rezervacije.request.CancellationPolicyRequest;
import org.example.rezervacije.response.CancellationPolicyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CancellationPolicyService {

    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final BookingRepository bookingRepository;

    public CancellationPolicyResponse save(CancellationPolicyRequest request) {
        CancellationPolicy policy = CancellationPolicyConverter.toEntity(request);
        CancellationPolicy saved = cancellationPolicyRepository.save(policy);
        return CancellationPolicyConverter.toResponse(saved);
    }

    public List<CancellationPolicyResponse> findAll() {
        return cancellationPolicyRepository.findAll()
                .stream()
                .map(CancellationPolicyConverter::toResponse)
                .collect(Collectors.toList());
    }

    public CancellationPolicyResponse findByPolicyName(String policyName) {
        CancellationPolicy policy = cancellationPolicyRepository.findByPolicyName(policyName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cancellation policy not found"
                ));

        return CancellationPolicyConverter.toResponse(policy);
    }

    public CancellationPolicyResponse updateByPolicyName(String policyName, CancellationPolicyRequest request) {
        CancellationPolicy policy = cancellationPolicyRepository.findByPolicyName(policyName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cancellation policy not found"
                ));

        policy.setPolicyName(request.getPolicyName());
        policy.setRefundPercentage(request.getRefundPercentage());

        CancellationPolicy updated = cancellationPolicyRepository.save(policy);
        return CancellationPolicyConverter.toResponse(updated);
    }

    @Transactional
    public void deleteByPolicyName(String policyName) {
        CancellationPolicy policy = cancellationPolicyRepository.findByPolicyName(policyName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cancellation policy not found"
                ));

        if (bookingRepository.existsByPolicy_Id(policy.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cancellation policy cannot be deleted because it is used in bookings."
            );
        }

        cancellationPolicyRepository.delete(policy);
    }
}