package web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.Occupant;
import web.repository.OccupantRepository;

@Service("occupantService")
public class OccupantService implements CrudService<Occupant>{

    private OccupantRepository occupantRepository;

    @Autowired
    public OccupantService(OccupantRepository occupantRepository) {
        this.occupantRepository = occupantRepository;
    }

    @Override
    public void add(Occupant entity) {
        occupantRepository.save(entity);
    }

    @Override
    public void update(Occupant entity) {
        occupantRepository.save(entity);
    }

    @Override
    public Occupant update(int id, Occupant entity) {
        return null;
    }

    @Override
    public void delete(Occupant entity) {
        occupantRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Occupant getById(int id) {
        return occupantRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid type Id:" + id));
    }

    @Override
    public List<Occupant> getAll() {
        return occupantRepository.findAll();
    }

    @Override
    public Page<Occupant> findAllPaginated(Pageable pageable) {
        return occupantRepository.findAll(pageable);
    }
}

