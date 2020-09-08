package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.Request;
import web.model.Specialization;
import web.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("requestService")
public class RequestService implements CrudService<Request> {

    private RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public void add(Request entity) {
        requestRepository.save(entity);
    }

    @Override
    public void update(Request entity) {
        requestRepository.save(entity);
    }

    public Request update(int id,Request entity) {
        return requestRepository.findById(id).map(request -> {
            if(entity.getEmployee()!=null) {
                request.setEmployee(entity.getEmployee());
            }
            if(entity.getDateOfDeadline()!=null) {
                request.setDateOfDeadline(entity.getDateOfDeadline());
            }
            request.setDateOfRequest(entity.getDateOfRequest());
            request.setProblem(entity.getProblem());
            return requestRepository.save(request);
        }).orElseGet(()->{
            entity.setId(id);
            return requestRepository.save(entity);
        });
    }

    @Override
    public void delete(Request entity) {
        requestRepository.delete(entity);
    }

    public void deleteById(int id) {
        requestRepository.deleteById(id);
    }

    public List<Request> getAllByType(Specialization specialization){
        return requestRepository.findAll().stream().filter(i->i.getEmployee()==null).filter(i->i.getSpecialization().equals(specialization)).collect(Collectors.toList());
    }

    public List<Request> getAllTypeByAdmin(){
        return requestRepository.findAll().stream().filter(i->i.getSpecialization().getName().equals("Администрация")).collect(Collectors.toList());
    }

    @Override
    public Request getById(int id) {
        return requestRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid type Id:" + id));
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    public List<Request> getAllByIdOccupant(int id) {
        return requestRepository.findAll().stream().filter(i->i.getOccupant().getId()==id).collect(Collectors.toList());
    }

    public List<Request> getAllByIdEmployee(int id) {
        List<Request> r = new ArrayList<>();
        List<Request> requests = requestRepository.findAll();
        for (Request request: requests) {
            if(request.getEmployee()!=null) {
                if (request.getEmployee().getId().equals(id)) {
                    r.add(request);
                }
            }

        }
        return r;
    }

    @Override
    public Page<Request> findAllPaginated(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }
}
