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
import web.model.Request;
import web.service.EmployeeService;
import web.service.OccupantService;
import web.service.RequestService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/request/")
public class RequestViewController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private OccupantService occupantService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * добавление заявки
     */
    @PostMapping("add")
    public String add(@Valid Request request, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("occupants", occupantService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("request", request);
            return "request/add-request";
        }
        requestService.add(request);
        return "redirect:list";
    }

    /*
     * отобразить страницу с формой ввода для добаления заявик
     */
    @GetMapping("addForm")
    public String showAddForm(Request request, Model model) {
        model.addAttribute("occupants", occupantService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        return "request/add-request";
    }

    /**
     * отобразить страницу с формой ввода для изменения заявок
     */
    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Request request = requestService.getById(id);
        model.addAttribute("occupants", occupantService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("request", request);
        return "request/update-request";
    }

    /**
     * изменение информации о заявке
     */
    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid Request request,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort = "dateOfRequest") Pageable pageable) {
        if (result.hasErrors()) {
            request.setId(id);
            model.addAttribute("occupants", occupantService.getAll());
            model.addAttribute("employees", employeeService.getAll());
            model.addAttribute("request", request);
            return "request/update-request";
        }
        requestService.update(request);
        Page<Request> page = requestService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "request/index-request";
    }

    /**
     * удаление заявки
     */
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,
                         @PageableDefault(size = 10, sort = "dateOfRequest") Pageable pageable) {
        Request request = requestService.getById(id);
        requestService.delete(request);
        Page<Request> page = requestService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "request/index-request";
    }

    /**
     * получить все заявки
     */
    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="dateOfRequest") Pageable pageable, Request request, HttpSession httpSession){
        Page<Request> page = requestService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "request/index-request";
    }


    private Model sort(Page<Request> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}
