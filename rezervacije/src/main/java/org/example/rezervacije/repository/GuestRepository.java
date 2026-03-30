package org.example.rezervacije.repository;

import jakarta.transaction.Transactional;
import org.example.rezervacije.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByPhoneNumber(String phoneNumber);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}