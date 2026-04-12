package org.example.rezervacije.service;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.GuestConverter;
import org.example.rezervacije.entity.Guest;
import org.example.rezervacije.repository.GuestRepository;
import org.example.rezervacije.request.GuestRequest;
import org.example.rezervacije.response.GuestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestResponse save(GuestRequest guestRequest) {
        Guest guest = GuestConverter.toEntity(guestRequest);
        Guest saved = guestRepository.save(guest);
        return GuestConverter.toResponse(saved);
    }

    public List<GuestResponse> findAll() {
        return guestRepository.findAll()
                .stream()
                .map(GuestConverter::toResponse)
                .collect(Collectors.toList());
    }

    public GuestResponse findByEmail(String email) {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        return GuestConverter.toResponse(guest);
    }

    public GuestResponse findByPhoneNumber(String phoneNumber) {
        Guest guest = guestRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        return GuestConverter.toResponse(guest);
    }

    public GuestResponse updateByEmail(String email, GuestRequest request) {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        guest.setFirstName(request.getFirstName());
        guest.setLastName(request.getLastName());
        guest.setEmail(request.getEmail());
        guest.setPhoneNumber(request.getPhoneNumber());

        Guest updated = guestRepository.save(guest);
        return GuestConverter.toResponse(updated);
    }

    @Transactional
    public void deleteByEmail(String email) {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        guestRepository.delete(guest);
    }

    @Transactional
    public void deleteByPhoneNumber(String phoneNumber) {
        Guest guest = guestRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guest not found"
                ));

        guestRepository.delete(guest);
    }
}