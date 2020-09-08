package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Employee;
import web.model.User;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findEmployeeBySurname(String surname);
}
