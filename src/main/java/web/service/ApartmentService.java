package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.Apartment;
import web.repository.ApartmentRepository;

import java.util.List;

@Service("apartmentService")
public class ApartmentService implements CrudService<Apartment> {

    private ApartmentRepository apartmentRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public void add(Apartment entity) {
        apartmentRepository.save(entity);
    }

    @Override
    public void update(Apartment entity) {
        apartmentRepository.save(entity);
    }

    @Override
    public Apartment update(int id, Apartment entity) {
        return apartmentRepository.findById(id).map(apartment -> {
            apartment.setBlock(entity.getBlock());
            apartment.setFloor(entity.getFloor());
            apartment.setNumber(entity.getNumber());
            return apartmentRepository.save(apartment);
        }).orElseGet(()->{
            entity.setId(id);
            return apartmentRepository.save(entity);
        });
    }

    @Override
    public void delete(Apartment entity) {
        apartmentRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public Apartment getById(int id) {
        return apartmentRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid type Id:" + id));
    }

    @Override
    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }

    @Override
    public Page<Apartment> findAllPaginated(Pageable pageable) {
        return apartmentRepository.findAll(pageable);
    }
}
