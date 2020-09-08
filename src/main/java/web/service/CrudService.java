package web.service;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CrudService<T> {

    void add(T entity);

    void update(T entity);

    T update(int id, T entity);

    void delete(T entity);

    void deleteById(int id);

    T getById(int id);

    List<T> getAll();

    Page<T> findAllPaginated(Pageable pageable);
}
