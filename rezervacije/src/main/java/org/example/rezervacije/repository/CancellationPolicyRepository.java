package org.example.rezervacije.repository;

import jakarta.transaction.Transactional;
import org.example.rezervacije.entity.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {
    Optional<CancellationPolicy> findByPolicyName(String policyName);

    @Transactional
    void deleteByPolicyName(String policyName);
}
