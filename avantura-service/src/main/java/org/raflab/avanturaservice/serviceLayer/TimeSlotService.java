package org.raflab.avanturaservice.serviceLayer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.raflab.avanturaservice.dtoLayer.request.TimeSlotRequest;
import org.raflab.avanturaservice.dtoLayer.response.TimeSlotResponse;
import org.raflab.avanturaservice.entityLayer.Adventure;
import org.raflab.avanturaservice.entityLayer.TimeSlot;
import org.raflab.avanturaservice.repositoryLayer.AdventureRepository;
import org.raflab.avanturaservice.utils.TimeSlotEntityMapper;
import org.springframework.stereotype.Service;
import org.raflab.avanturaservice.repositoryLayer.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final AdventureRepository adventureRepository;
    private final TimeSlotEntityMapper mapper;

    @Transactional
    public TimeSlotResponse createTimeSlot(TimeSlotRequest request) {
        // 1. Logička provera vremena (VRLO BITNO)
        validateTimeLogic(request.getStartTime(), request.getEndTime());

        // 2. Provera avanture
        Adventure adventure = adventureRepository.findById(request.getAdventureId())
                .orElseThrow(() -> new RuntimeException("Greška: Avantura nije pronađena."));

        TimeSlot timeSlot = mapper.toEntity(request, adventure);
        return mapper.toResponse(timeSlotRepository.save(timeSlot));
    }

    public List<TimeSlotResponse> getAllTimeSlots() {
        return timeSlotRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public TimeSlotResponse getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Termin ID " + id + " nije pronađen."));
    }

    @Transactional
    public TimeSlotResponse updateTimeSlot(Long id, TimeSlotRequest request) {
        validateTimeLogic(request.getStartTime(), request.getEndTime());

        TimeSlot existing = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Termin ne postoji."));

        // Provera da li već ima prijavljenih ljudi - ako ima, ne smemo smanjiti kapacitet ispod tog broja
        if (request.getMaxParticipants() < existing.getCurrentOccupancy()) {
            throw new RuntimeException("Greška: Maksimalan broj učesnika ne može biti manji od trenutno prijavljenih (" + existing.getCurrentOccupancy() + ").");
        }

        existing.setStartTime(request.getStartTime());
        existing.setEndTime(request.getEndTime());
        existing.setMaxParticipants(request.getMaxParticipants());

        return mapper.toResponse(timeSlotRepository.save(existing));
    }

    @Transactional
    public void deleteTimeSlot(Long id) {
        TimeSlot slot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Termin nije pronađen."));

        // Logička kontrola: Ne briši termin koji već ima prijavljene ljude
        if (slot.getCurrentOccupancy() > 0) {
            throw new RuntimeException("Greška: Ne možete obrisati termin u kojem već ima prijavljenih učesnika.");
        }

        timeSlotRepository.deleteById(id);
    }

    // --- LOGIČKA VALIDACIJA VREMENA ---
    private void validateTimeLogic(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new RuntimeException("Greška: Vremena početka i završetka moraju biti definisana.");
        }
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new RuntimeException("Greška: Vreme završetka mora biti nakon vremena početka.");
        }
        // Minimalno trajanje avanture 30 minuta
        if (java.time.Duration.between(start, end).toMinutes() < 30) {
            throw new RuntimeException("Greška: Avantura ne može trajati kraće od 30 minuta.");
        }
    }

    @Transactional
    public void bookSpotInTimeSlot(Long slotId) {
        TimeSlot slot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Greška: Termin nije pronađen."));

        if (slot.getCurrentOccupancy() >= slot.getMaxParticipants()) {
            throw new RuntimeException("Greška: Nema više slobodnih mesta u ovom terminu (" + slot.getMaxParticipants() + " max).");
        }

        slot.setCurrentOccupancy(slot.getCurrentOccupancy() + 1);
        timeSlotRepository.save(slot);
    }

    public List<TimeSlotResponse> getAvailableTimeSlots() {
        return timeSlotRepository.findAll().stream()
                .filter(slot -> slot.getCurrentOccupancy() < slot.getMaxParticipants())
                .filter(slot -> slot.getStartTime().isAfter(LocalDateTime.now()))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
