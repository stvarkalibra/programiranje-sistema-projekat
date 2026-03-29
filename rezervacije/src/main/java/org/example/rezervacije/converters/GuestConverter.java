package org.example.rezervacije.converters;

import org.example.rezervacije.entity.Guest;
import org.example.rezervacije.request.GuestRequest;
import org.example.rezervacije.response.GuestResponse;

public class GuestConverter {
    public static GuestResponse toResponse(Guest guest) {
        GuestResponse response = new GuestResponse(
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhoneNumber()
                );
        return response;
    }

    public static Guest toEntity(GuestRequest request) {
        Guest guest = new Guest();
        guest.setFirstName(request.getFirstName());
        guest.setLastName(request.getLastName());
        guest.setEmail(request.getEmail());
        guest.setPhoneNumber(request.getPhoneNumber());
        return guest;
    }
}
