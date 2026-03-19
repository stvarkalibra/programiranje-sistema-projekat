package org.raflab.guideservice.serviceLayer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.guideservice.clientLayer.AdventureClient;
import org.raflab.guideservice.clientLayer.RemoteAdventureDTO;
import org.raflab.guideservice.dtoLayer.request.GuideProfileRequest;
import org.raflab.guideservice.dtoLayer.response.GuideProfileResponse;
import org.raflab.guideservice.entityLayer.GuideProfile;
import org.raflab.guideservice.repositoryLayer.CertificationRepository;
import org.raflab.guideservice.repositoryLayer.GuideLanguageRepository;
import org.raflab.guideservice.repositoryLayer.GuideProfileRepository;
import org.raflab.guideservice.utils.GuideProfileEntityMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideProfileService {

    private final GuideProfileRepository guideRepository;
    private final GuideProfileEntityMapper mapper;
    private final CertificationRepository certificationRepository;
    private final GuideLanguageRepository guideLanguageRepository;
    private final AdventureClient adventureClient;

    @Transactional
    public GuideProfileResponse createGuide(GuideProfileRequest request) {
        if (guideRepository.existsByContactEmail(request.getContactEmail())) {
            throw new RuntimeException("Greška: Email '" + request.getContactEmail() + "' je zauzet.");
        }
        GuideProfile guide = mapper.toEntity(request);
        return mapper.toResponse(guideRepository.save(guide));
    }

    public List<GuideProfileResponse> getAllGuides() {
        return guideRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public GuideProfileResponse getGuideById(Long id) {
        return guideRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Vodič ID " + id + " ne postoji."));
    }

    @Transactional
    public GuideProfileResponse updateGuide(Long id, GuideProfileRequest request) {
        GuideProfile existing = guideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vodič ID " + id + " ne postoji."));

        // Validacija Emaila
        if (!existing.getContactEmail().equals(request.getContactEmail()) &&
                guideRepository.existsByContactEmail(request.getContactEmail())) {
            throw new RuntimeException("Greška: Email '" + request.getContactEmail() + "' je zauzet.");
        }

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setBio(request.getBio());
        existing.setContactEmail(request.getContactEmail());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setRating(request.getRating()); // Dodato ažuriranje rejtinga kroz request

        return mapper.toResponse(guideRepository.save(existing));
    }

    @Transactional
    public void deleteGuide(Long id) {
        if (!guideRepository.existsById(id)) {
            throw new RuntimeException("Brisanje neuspelo, ID " + id + " nije u bazi.");
        }
        guideRepository.deleteById(id);
    }

    public List<GuideProfileResponse> getEliteGuides() {
        return guideRepository.findEliteGuides().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<GuideProfileResponse> findReplacements(Long languageId) {
        return guideRepository.findAllByLanguageId(languageId).stream()
                .filter(g -> g.getRating() >= 4.0)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public boolean isGuideReadyForField(Long guideId) {
        GuideProfile guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new RuntimeException("Vodič nije pronađen"));
        long validCerts = certificationRepository.countValidCertifications(guideId, LocalDate.now());
        return guide.getRating() >= 4.0 && validCerts > 0;
    }

    @Transactional
    public GuideProfileResponse applyLanguageBonus(Long guideId) {
        GuideProfile guide = guideRepository.findById(guideId).orElseThrow();
        long langCount = guideLanguageRepository.countByGuideProfileId(guideId);

        if (langCount >= 3) {
            double newRating = Math.min(5.0, guide.getRating() + 0.5);
            guide.setRating(newRating);
        }
        return mapper.toResponse(guideRepository.save(guide));
    }

    public Map<String, Long> getRatingDistribution() {
        List<GuideProfile> all = guideRepository.findAll();
        return Map.of(
                "Gold (4.5+)", all.stream().filter(g -> g.getRating() >= 4.5).count(),
                "Silver (3.5-4.5)", all.stream().filter(g -> g.getRating() >= 3.5 && g.getRating() < 4.5).count(),
                "Bronze (<3.5)", all.stream().filter(g -> g.getRating() < 3.5).count()
        );
    }

    // 1. METODA: Premium Report + SLOŽENA PROVERA
    @CircuitBreaker(name = "adventureCB", fallbackMethod = "fallbackPremium")
    @Retry(name = "adventureRetry")
    public List<RemoteAdventureDTO> getPremiumReport(Long guideId, Double minPrice) {
        // Logika provere 1: Validacija ulaznih parametara pre poziva
        if (minPrice < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cena ne može biti negativna");

        List<RemoteAdventureDTO> list = adventureClient.getPremiumAdventures(guideId, minPrice);

        // Logika provere 2: Ako je lista sumnjivo velika (npr. > 100), loguj upozorenje
        if (list.size() > 100) System.out.println("WARN: Neobično veliki broj premium tura za vodiča " + guideId);

        return list;
    }

    // 2. METODA: Prihod + DODAT RETRY + PROVERA
    @CircuitBreaker(name = "adventureCB", fallbackMethod = "fallbackRevenue")
    @Retry(name = "adventureRetry") // Dodato zbog zahteva zadatka
    public Double getRevenue(Long guideId) {
        Double revenue = adventureClient.getGuideRevenue(guideId);

        // Logika provere 3: Provera anomalija u podacima
        if (revenue > 1000000) {
            System.out.println("ALERT: Detektovan nerealno visok prihod!");
        }
        return revenue;
    }

    // 3. METODA: Overload + DODAT RETRY + PROVERA
    @CircuitBreaker(name = "adventureCB", fallbackMethod = "fallbackOverload")
    @Retry(name = "adventureRetry") // Dodato zbog zahteva zadatka
    public Boolean checkOverload(Long guideId) {
        // Ovde možemo dodati proveru da li vodič uopšte postoji u našoj bazi pre poziva drugog servisa
        // To bi bila "složenija provera podataka pre daljeg procesiranja"
        return adventureClient.isGuideOverloaded(guideId);
    }

    @CircuitBreaker(name = "adventureCB", fallbackMethod = "fallbackCategoryOnly")
    @Retry(name = "adventureRetry")
    public List<RemoteAdventureDTO> getAllInCategory(Long categoryId) {
        // Složena provera: Validacija ID-a
        if (categoryId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nevalidan ID kategorije.");
        }

        List<RemoteAdventureDTO> adventures = adventureClient.getByCategory(categoryId);

        // Dodatna logika: Ako nema avantura, logujemo informaciju
        if (adventures.isEmpty()) {
            System.out.println("INFO: Kategorija " + categoryId + " trenutno nema aktivnih avantura.");
        }

        return adventures;
    }

    public List<RemoteAdventureDTO> fallbackCategoryOnly(Long categoryId, Throwable t) {
        return new ArrayList<>();
    }

    public List<RemoteAdventureDTO> fallbackPremium(Long guideId, Double minPrice, Throwable t) {
        System.out.println("LOG: Avantura-service nedostupan za premium report. Razlog: " + t.getMessage());
        return new ArrayList<>(); // Vraća praznu listu umesto greške 500
    }

    public Double fallbackRevenue(Long guideId, Throwable t) {
        return 0.0; // Vraća nulu ako servis ne radi
    }

    public Boolean fallbackOverload(Long guideId, Throwable t) {
        return false; // Pretpostavljamo da nije preopterećen ako ne možemo da proverimo
    }
}