package org.raflab.avanturaservice.serviceLayer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.raflab.avanturaservice.dtoLayer.request.GearRequirementRequest;
import org.raflab.avanturaservice.dtoLayer.response.GearRequirementResponse;
import org.raflab.avanturaservice.entityLayer.Adventure;
import org.raflab.avanturaservice.entityLayer.GearRequirement;
import org.raflab.avanturaservice.repositoryLayer.AdventureRepository;
import org.raflab.avanturaservice.repositoryLayer.GearRequirementRepository;
import org.raflab.avanturaservice.utils.GearRequirementEntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GearRequirementService {

    private final GearRequirementRepository gearRepository;
    private final AdventureRepository adventureRepository;
    private final GearRequirementEntityMapper mapper;

    @Transactional
    public GearRequirementResponse createGearRequirement(GearRequirementRequest request) {
        // 1. Validacija unosa
        validateGearRequest(request);

        // 2. Provera postojanja avanture
        Adventure adventure = adventureRepository.findById(request.getAdventureId())
                .orElseThrow(() -> new RuntimeException("Greška: Avantura sa ID " + request.getAdventureId() + " nije pronađena."));

        // 3. Provera duplikata (da ne dodajemo istu opremu istoj avanturi više puta)
        if (gearRepository.existsByItemNameAndAdventureId(request.getItemName(), request.getAdventureId())) {
            throw new RuntimeException("Greška: Oprema '" + request.getItemName() + "' je već dodata za ovu avanturu.");
        }

        GearRequirement gear = mapper.toEntity(request, adventure);
        return mapper.toResponse(gearRepository.save(gear));
    }

    public List<GearRequirementResponse> getAllGearRequirements() {
        return gearRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public GearRequirementResponse getGearRequirementById(Long id) {
        GearRequirement gear = gearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oprema ID " + id + " nije pronađena."));
        return mapper.toResponse(gear);
    }

    @Transactional
    public GearRequirementResponse updateGearRequirement(Long id, GearRequirementRequest request) {
        validateGearRequest(request);

        GearRequirement existing = gearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oprema ID " + id + " ne postoji."));

        // Ako se menja avantura
        if (!existing.getAdventure().getId().equals(request.getAdventureId())) {
            Adventure newAdv = adventureRepository.findById(request.getAdventureId())
                    .orElseThrow(() -> new RuntimeException("Nova avantura nije pronađena."));
            existing.setAdventure(newAdv);
        }

        existing.setItemName(request.getItemName());
        existing.setMandatory(request.getIsMandatory());

        return mapper.toResponse(gearRepository.save(existing));
    }

    @Transactional
    public void deleteGearRequirement(Long id) {
        if (!gearRepository.existsById(id)) {
            throw new RuntimeException("Brisanje neuspelo: Stavka opreme ne postoji.");
        }
        gearRepository.deleteById(id);
    }

    // --- DODATNA VALIDACIJA ---
    private void validateGearRequest(GearRequirementRequest request) {
        if (request.getItemName() == null || request.getItemName().trim().isEmpty()) {
            throw new RuntimeException("Greška: Naziv opreme ne sme biti prazan.");
        }
        // Primer poslovne logike: zabraniti nazive koji su samo brojevi
        if (request.getItemName().matches("-?\\d+(\\.\\d+)?")) {
            throw new RuntimeException("Greška: Naziv opreme ne može biti samo broj.");
        }
    }

    public String checkSafetyGearCompliance(Long adventureId) {
        Adventure adventure = adventureRepository.findById(adventureId)
                .orElseThrow(() -> new RuntimeException("Avantura nije nađena."));

        long mandatoryGearCount = gearRepository.findAll().stream()
                .filter(g -> g.getAdventure().getId().equals(adventureId) && g.isMandatory())
                .count();

        if ("Extreme".equalsIgnoreCase(adventure.getDifficultyLevel()) && mandatoryGearCount < 3) {
            return "UPOZORENJE: Extreme avantura mora imati barem 3 stavke obavezne opreme! Trenutno: " + mandatoryGearCount;
        }
        return "Oprema je u skladu sa nivoom težine.";
    }

    /**
     * Metoda vrši matematičku kalkulaciju (prebrojavanje) obaveznih stavki opreme
     * za specifičnu avanturu.
     */
    public Long countMandatoryGear(Long adventureId) {
        // Prvo proveravamo da li avantura postoji da ne bismo radili kalkulaciju nad nepostojećim ID-em
        if (!adventureRepository.existsById(adventureId)) {
            throw new RuntimeException("Greška: Avantura sa ID " + adventureId + " nije pronađena.");
        }

        // Filtriramo svu opremu koja pripada toj avanturi i koja je označena kao obavezna
        return gearRepository.findAll().stream()
                .filter(gear -> gear.getAdventure().getId().equals(adventureId))
                .filter(gear -> gear.isMandatory()) // isMandatory mora biti true
                .count(); // Vraća ukupan broj (Long)
    }
}