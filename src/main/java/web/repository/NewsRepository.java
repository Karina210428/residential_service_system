package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
