package org.raflab.guideservice.serviceLayer;

import lombok.RequiredArgsConstructor;
import org.raflab.guideservice.dtoLayer.request.LanguageRequest;
import org.raflab.guideservice.dtoLayer.response.LanguageResponse;
import org.raflab.guideservice.entityLayer.Language;
import org.raflab.guideservice.repositoryLayer.LanguageRepository;
import org.raflab.guideservice.utils.LanguageEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageEntityMapper mapper;

    @Transactional
    public LanguageResponse createLanguage(LanguageRequest request) {
        // 1. Provera da li jezik sa tim imenom već postoji
        if (languageRepository.existsByLanguageName(request.getLanguageName())) {
            throw new RuntimeException("Greška: Jezik '" + request.getLanguageName() + "' je već u katalogu.");
        }

        // 2. Provera da li ISO kod već postoji
        if (languageRepository.existsByLanguageCode(request.getLanguageCode())) {
            throw new RuntimeException("Greška: ISO kod '" + request.getLanguageCode() + "' već koristi drugi jezik.");
        }

        Language language = mapper.toEntity(request);
        return mapper.toResponse(languageRepository.save(language));
    }

    public List<LanguageResponse> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public LanguageResponse getLanguageById(Long id) {
        return languageRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Greška: Jezik sa ID " + id + " nije pronađen."));
    }

    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language existing = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greška: Izmena nemoguća, jezik ID " + id + " ne postoji."));

        // Validacija za unikatnost pri izmeni
        if (!existing.getLanguageName().equals(request.getLanguageName()) &&
                languageRepository.existsByLanguageName(request.getLanguageName())) {
            throw new RuntimeException("Greška: Naziv jezika je već zauzet.");
        }

        existing.setLanguageName(request.getLanguageName());
        existing.setLanguageCode(request.getLanguageCode());

        return mapper.toResponse(languageRepository.save(existing));
    }

    @Transactional
    public void deleteLanguage(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new RuntimeException("Greška: Brisanje neuspelo, jezik nije u bazi.");
        }
        languageRepository.deleteById(id);
    }
}