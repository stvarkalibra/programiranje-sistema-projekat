package org.example.rezervacije.service;

import lombok.RequiredArgsConstructor;
import org.example.rezervacije.converters.AddOnItemConverter;
import org.example.rezervacije.entity.AddOnItem;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.repository.AddOnItemRepository;
import org.example.rezervacije.repository.BookingRepository;
import org.example.rezervacije.request.AddOnItemRequest;
import org.example.rezervacije.response.AddOnItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddOnItemService {

    private final AddOnItemRepository addOnItemRepository;
    private final BookingRepository bookingRepository;

    public AddOnItemResponse save(AddOnItemRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        AddOnItem addOnItem = AddOnItemConverter.toEntity(request, booking);
        AddOnItem saved = addOnItemRepository.save(addOnItem);

        return AddOnItemConverter.toResponse(saved);
    }

    public List<AddOnItemResponse> findAll() {
        return addOnItemRepository.findAll()
                .stream()
                .map(AddOnItemConverter::toResponse)
                .collect(Collectors.toList());
    }

    public AddOnItemResponse findById(Long id) {
        AddOnItem addOnItem = addOnItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "AddOnItem not found"
                ));

        return AddOnItemConverter.toResponse(addOnItem);
    }

    public List<AddOnItemResponse> findByBookingId(Long bookingId) {
        return addOnItemRepository.findByBooking_Id(bookingId)
                .stream()
                .map(AddOnItemConverter::toResponse)
                .collect(Collectors.toList());
    }

    public AddOnItemResponse update(Long id, AddOnItemRequest request) {
        AddOnItem addOnItem = addOnItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "AddOnItem not found"
                ));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));

        addOnItem.setBooking(booking);
        addOnItem.setServiceName(request.getServiceName());
        addOnItem.setPrice(request.getPrice());

        AddOnItem updated = addOnItemRepository.save(addOnItem);
        return AddOnItemConverter.toResponse(updated);
    }

    public void deleteById(Long id) {
        AddOnItem addOnItem = addOnItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "AddOnItem not found"
                ));

        addOnItemRepository.delete(addOnItem);
    }
}