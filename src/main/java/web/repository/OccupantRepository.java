package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Occupant;

public interface OccupantRepository extends JpaRepository<Occupant, Integer> {
}
