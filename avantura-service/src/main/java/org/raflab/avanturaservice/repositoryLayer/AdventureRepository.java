package org.raflab.avanturaservice.repositoryLayer;

import org.raflab.avanturaservice.entityLayer.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure,Long> {
    // Metoda koja proverava da li naslov vec postoji (vraca true/false)
    boolean existsByTitle(String title);

    // Bonus: Metoda koja ce ti trebati za filtriranje po kategoriji i ceni (zahtev iz zadatka)
    List<Adventure> findByAdventureCategoryIdAndBasePriceLessThanEqual(Long categoryId, Double maxPrice);
}
