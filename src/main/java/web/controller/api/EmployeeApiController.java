package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.model.Employee;
import web.model.Location;
import web.model.User;
import web.service.EmployeeService;
import web.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/employee/")
public class EmployeeApiController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "add",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Employee> add(@RequestBody Employee employee){
        employeeService.add(employee);
        return employeeService.getAll();
    }

    @RequestMapping(value = "sentLocation",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity setLocation(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Location location){
        User user = userService.findUserByUsername(userDetails.getUsername());
        Employee employee = user.getEmployee();
        employee.setLatitude(location.getLatitude());
        employee.setLongitude(location.getLongitude());
        Employee newLocation = employeeService.updateLocation(employee);
        return ResponseEntity.ok(newLocation);
    }

    @RequestMapping(value = "update/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public Employee update(@RequestBody Employee employee, @PathVariable int id) {
        return employeeService.update(id, employee);
    }

    @RequestMapping(value = "delete/{id}",
            method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        employeeService.deleteById(id);
    }

    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Employee> getAll(@AuthenticationPrincipal UserDetails userDetails){
        return employeeService.getAll();
    }

}
