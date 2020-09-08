package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
}
