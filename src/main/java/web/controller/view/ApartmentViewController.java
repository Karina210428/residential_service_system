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
import web.model.Apartment;
import web.service.ApartmentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/apartment/")
public class ApartmentViewController {

    @Autowired
    private ApartmentService apartmentService;

    @PostMapping("add")
    public String add(@Valid Apartment apartment, BindingResult result, Model model){
        if(result.hasErrors()){
            return "apartment/add-apartment";
        }
        apartmentService.add(apartment);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(Apartment apartment, Model model) {
        return "apartment/add-apartment";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Apartment apartment = apartmentService.getById(id);
        model.addAttribute("apartment", apartment);
        return "apartment/update-apartment";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid Apartment apartment,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort = "number") Pageable pageable) {
        if (result.hasErrors()) {
            apartment.setId(id);
            return "apartment/update-apartment";
        }
        apartmentService.update(apartment);
        Page<Apartment> page = apartmentService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "apartment/index-apartment";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,  @PageableDefault(size = 10, sort="number") Pageable pageable) {
        Apartment apartment = apartmentService.getById(id);
        apartmentService.delete(apartment);
        Page<Apartment> page = apartmentService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "apartment/index-apartment";
    }

    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="number") Pageable pageable,  Apartment apartment){
        Page<Apartment> page = apartmentService.findAllPaginated(pageable);
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        model.addAttribute("page", page);
        return "apartment/index-apartment";
    }

    private Model sort(Page<Apartment> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}
