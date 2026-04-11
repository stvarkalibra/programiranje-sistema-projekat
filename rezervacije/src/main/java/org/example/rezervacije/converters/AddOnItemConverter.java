package org.example.rezervacije.converters;

import org.example.rezervacije.entity.AddOnItem;
import org.example.rezervacije.entity.Booking;
import org.example.rezervacije.request.AddOnItemRequest;
import org.example.rezervacije.response.AddOnItemResponse;

public class AddOnItemConverter {

    public static AddOnItemResponse toResponse(AddOnItem addOnItem) {

        AddOnItemResponse response = new AddOnItemResponse();

        response.setBookingId(addOnItem.getBooking().getId());
        response.setServiceName(addOnItem.getServiceName());
        response.setPrice(addOnItem.getPrice());

        return response;
    }

    public static AddOnItem toEntity(AddOnItemRequest request, Booking booking) {

        AddOnItem addOnItem = new AddOnItem();
        addOnItem.setBooking(booking);
        addOnItem.setServiceName(request.getServiceName());
        addOnItem.setPrice(request.getPrice());

        return addOnItem;
    }
}