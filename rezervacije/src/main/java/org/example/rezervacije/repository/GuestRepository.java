package org.example.rezervacije.repository;

import jakarta.transaction.Transactional;
import org.example.rezervacije.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("select g from Guest g where g.email = :guestEmail")
    Optional<Guest> findByEmail(String guestEmail);

    @Query("select g from Guest g where g.phoneNumber = :guestPhoneNumber")
    Optional<Guest> findByPhoneNumber(String phoneNumber);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}
