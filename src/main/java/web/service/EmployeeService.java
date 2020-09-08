package web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.Employee;
import web.model.User;
import web.repository.EmployeeRepository;

@Service("employeeService")
public class EmployeeService implements CrudService<Employee>{

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void add(Employee entity) {
        employeeRepository.save(entity);
    }

    @Override
    public void update(Employee entity) {
        employeeRepository.save(entity);
    }

    public Employee updateLocation(Employee entity) {
        return  employeeRepository.save(entity);
    }

    @Override
    public Employee update(int id, Employee entity) {
        return null;
    }

    @Override
    public void delete(Employee entity) {
        employeeRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    public Employee findEmployeeBySurname(String username) {
        return employeeRepository.findEmployeeBySurname(username);
    }


    @Override
    public Employee getById(int id) {
        return employeeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid type Id:" + id));
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> findAllPaginated(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}
