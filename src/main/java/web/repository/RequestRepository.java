package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
