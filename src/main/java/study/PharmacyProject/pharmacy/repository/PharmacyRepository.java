package study.PharmacyProject.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.PharmacyProject.pharmacy.entity.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
