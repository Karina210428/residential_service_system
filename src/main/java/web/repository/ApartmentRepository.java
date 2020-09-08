package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
}
