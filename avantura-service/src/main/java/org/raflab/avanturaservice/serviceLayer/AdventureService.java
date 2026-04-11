package org.raflab.avanturaservice.serviceLayer;

import lombok.RequiredArgsConstructor;
import org.raflab.avanturaservice.dtoLayer.request.AdventureRequest;
import org.raflab.avanturaservice.dtoLayer.response.AdventureResponse;
import org.raflab.avanturaservice.entityLayer.Adventure;
import org.raflab.avanturaservice.entityLayer.AdventureCategory;
import org.raflab.avanturaservice.repositoryLayer.AdventureCategoryRepository;
import org.raflab.avanturaservice.repositoryLayer.AdventureRepository;
import org.raflab.avanturaservice.utils.AdventureEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdventureService {

    private final AdventureRepository adventureRepository;
    private final AdventureCategoryRepository categoryRepository;
    private final AdventureEntityMapper adventureEntityMapper;

    // CREATE - Potpuno kontrolisan unos
    @Transactional
    public AdventureResponse createAdventure(AdventureRequest request) {
        // 1. Provera postojanja kategorije (Referentni integritet)
        AdventureCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Greška: Kategorija sa ID " + request.getCategoryId() + " nije pronađena."));

        // 2. Provera logičkog integriteta: Duplikat naslova
        if (adventureRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Greška: Avantura sa naslovom '" + request.getTitle() + "' već postoji.");
        }

        // 3. Poslovna logika: Primer - Extreme avanture ne mogu biti jeftine
        if ("Extreme".equalsIgnoreCase(request.getDifficultyLevel()) && request.getBasePrice() < 150.0) {
            throw new RuntimeException("Greška: Avanture sa nivoom 'Extreme' moraju imati bazičnu cenu od minimalno 150.0.");
        }

        Adventure adventure = adventureEntityMapper.toEntity(request, category);
        Adventure saved = adventureRepository.save(adventure);
        return adventureEntityMapper.toResponse(saved);
    }

    // READ ALL
    public List<AdventureResponse> getAllAdventures() {
        return adventureRepository.findAll().stream()
                .map(adventureEntityMapper::toResponse)
                .collect(Collectors.toList());
    }

    // READ ONE
    public AdventureResponse getAdventureById(Long id) {
        return adventureRepository.findById(id)
                .map(adventureEntityMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Greška: Avantura sa ID " + id + " ne postoji."));
    }

    // UPDATE - Kontrolisana izmena
    @Transactional
    public AdventureResponse updateAdventure(Long id, AdventureRequest request) {
        // 1. Provera da li entitet uopšte postoji
        Adventure existing = adventureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greška: Izmena nemoguća, avantura sa ID " + id + " ne postoji."));

        // 2. Provera kategorije
        AdventureCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Greška: Kategorija nije pronađena."));

        // 3. Provera duplikata naslova (ako se naslov menja, proveri da li novi već zauzet)
        if (!existing.getTitle().equals(request.getTitle()) && adventureRepository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Greška: Naslov '" + request.getTitle() + "' je već zauzet od strane druge avanture.");
        }

        // 4. Ponovna validacija poslovne logike cene
        if ("Extreme".equalsIgnoreCase(request.getDifficultyLevel()) && request.getBasePrice() < 150.0) {
            throw new RuntimeException("Greška: Nije moguće postaviti cenu ispod 150.0 za Extreme nivo.");
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setDifficultyLevel(request.getDifficultyLevel());
        existing.setBasePrice(request.getBasePrice());
        existing.setAdventureCategory(category);

        return adventureEntityMapper.toResponse(adventureRepository.save(existing));
    }

    // DELETE
    @Transactional
    public void deleteAdventure(Long id) {
        // Provera pre brisanja sprečava neočekivane SQL greške
        if (!adventureRepository.existsById(id)) {
            throw new RuntimeException("Greška: Brisanje neuspelo, avantura sa ID " + id + " nije u bazi.");
        }
        adventureRepository.deleteById(id);
    }

    public Double calculateAdventurePrice(Long id, Double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new RuntimeException("Greška: Popust mora biti između 0 i 100%.");
        }
        Adventure a = adventureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greška: Avantura nije nađena."));

        double discountedPrice = a.getBasePrice() * (1 - discountPercentage / 100);
        return discountedPrice;
    }

    public List<AdventureResponse> filterAdventures(Long categoryId, Double maxPrice, String difficulty) {
        return adventureRepository.findAll().stream()
                .filter(a -> (categoryId == null || a.getAdventureCategory().getId().equals(categoryId)))
                .filter(a -> (maxPrice == null || a.getBasePrice() <= maxPrice))
                .filter(a -> (difficulty == null || a.getDifficultyLevel().equalsIgnoreCase(difficulty)))
                .map(adventureEntityMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 1. Metoda: Premium avanture (Filter na strani drugog servisa)
    public List<AdventureResponse> getPremiumAdventures(Long guideId, Double minPrice) {
        return adventureRepository.findByGuideId(guideId).stream()
                .filter(a -> a.getBasePrice() >= minPrice)
                .map(adventureEntityMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 2. Metoda: Ukupan prihod (Agregacija)
    public Double getTotalGuideRevenue(Long guideId) {
        return adventureRepository.findByGuideId(guideId).stream()
                .mapToDouble(Adventure::getBasePrice)
                .sum();
    }

    // 3. Metoda: Overload (Logička provera)
    public Boolean isGuideOverloaded(Long guideId) {
        // Overload je ako ima više od 3 avanture
        return adventureRepository.findByGuideId(guideId).size() > 3;
    }

    public List<AdventureResponse> getAdventuresByCategory(Long categoryId) {
        return adventureRepository.findByCategoryId(categoryId).stream()
                .map(adventureEntityMapper::toResponse)
                .collect(Collectors.toList());
    }
}