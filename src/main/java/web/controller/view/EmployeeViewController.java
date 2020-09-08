package web.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.Employee;
import web.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee/")
public class EmployeeViewController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("add")
    public String add(@Valid Employee employee, BindingResult result, Model model){
        if(result.hasErrors()){
            return "employee/add-employee";
        }
        employeeService.add(employee);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(Employee employee) {
        return "employee/add-employee";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Employee employee = employeeService.getById(id);
        model.addAttribute("employee", employee);
        return "employee/update-employee";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid Employee employee,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort="surname") Pageable pageable) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "employee/update-employee";
        }
        employeeService.update(employee);
        Page<Employee> page = employeeService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "employee/index-employee";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,
                         @PageableDefault(size = 10, sort="surname") Pageable pageable) {
        Employee employee = employeeService.getById(id);
        employeeService.delete(employee);
        Page<Employee> page = employeeService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "employee/index-employee";
    }

    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="surname") Pageable pageable, Employee employee){
        Page<Employee> page = employeeService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "employee/index-employee";
    }

    @GetMapping("moderation/{id}")
    public String moderation(@PathVariable("id") int id, Model model,
                             @PageableDefault(size = 10, sort = "surname") Pageable pageable) {
        Employee employee = employeeService.getById(id);
        employeeService.update(employee);
        Page<Employee> page = employeeService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "employee/index-employee";
    }

    private Model sort(Page<Employee> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}
