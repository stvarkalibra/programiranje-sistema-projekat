package org.raflab.guideservice.serviceLayer;

import lombok.RequiredArgsConstructor;
import org.raflab.guideservice.dtoLayer.request.CertificationRequest;
import org.raflab.guideservice.dtoLayer.response.CertificationResponse;
import org.raflab.guideservice.entityLayer.Certification;
import org.raflab.guideservice.entityLayer.GuideProfile;
import org.raflab.guideservice.repositoryLayer.CertificationRepository;
import org.raflab.guideservice.repositoryLayer.GuideProfileRepository;
import org.raflab.guideservice.utils.CertificationEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final GuideProfileRepository guideProfileRepository;
    private final CertificationEntityMapper certificationEntityMapper;

    @Transactional
    public CertificationResponse createCertification(CertificationRequest request) {
        GuideProfile guide = guideProfileRepository.findById(request.getGuideProfileId())
                .orElseThrow(() -> new RuntimeException("Vodič ID " + request.getGuideProfileId() + " ne postoji."));

        if (request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Sertifikat je već istekao.");
        }

        Certification certification = certificationEntityMapper.toEntity(request, guide);
        return certificationEntityMapper.toResponse(certificationRepository.save(certification));
    }

    public List<CertificationResponse> getAllCertifications() {
        return certificationRepository.findAll().stream()
                .map(certificationEntityMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CertificationResponse getCertificationById(Long id) {
        return certificationRepository.findById(id)
                .map(certificationEntityMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Sertifikat ID " + id + " ne postoji."));
    }

    @Transactional
    public CertificationResponse updateCertification(Long id, CertificationRequest request) {
        Certification existing = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sertifikat ne postoji."));

        GuideProfile guide = guideProfileRepository.findById(request.getGuideProfileId())
                .orElseThrow(() -> new RuntimeException("Vodič nije pronađen."));

        existing.setName(request.getName());
        existing.setIssuingBody(request.getIssuingBody());
        existing.setExpiryDate(request.getExpiryDate());
        existing.setGuideProfile(guide);

        return certificationEntityMapper.toResponse(certificationRepository.save(existing));
    }

    @Transactional
    public void deleteCertification(Long id) {
        if (!certificationRepository.existsById(id)) {
            throw new RuntimeException("Brisanje neuspelo.");
        }
        certificationRepository.deleteById(id);
    }

    public List<String> getGuidesWithExpiredCertificates() {
        return certificationRepository.findByExpiryDateBefore(LocalDate.now()).stream()
                .map(c -> c.getGuideProfile().getContactEmail())
                .distinct()
                .collect(Collectors.toList());
    }
}