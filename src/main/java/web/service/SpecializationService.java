package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.Specialization;
import web.repository.RequestRepository;
import web.repository.SpecializationRepository;

import java.util.List;

@Service("specializationService")
public class SpecializationService implements CrudService<Specialization>{

    private SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @Override
    public void add(Specialization entity) {
        specializationRepository.save(entity);
    }

    @Override
    public void update(Specialization entity) {
        specializationRepository.save(entity);
    }

    @Override
    public Specialization update(int id, Specialization entity) {
        return specializationRepository.save(entity);
    }

    @Override
    public void delete(Specialization entity) {
        specializationRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {
        specializationRepository.deleteById(id);
    }

    @Override
    public Specialization getById(int id) {
        return specializationRepository.getOne(id);
    }

    @Override
    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    @Override
    public Page<Specialization> findAllPaginated(Pageable pageable) {
        return specializationRepository.findAll(pageable);
    }
}
