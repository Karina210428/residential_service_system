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
import web.model.Specialization;
import web.service.SpecializationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialization/")
public class SpecializationViewController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping("add")
    public String add(@Valid Specialization specialization, BindingResult result, Model model){
        if(result.hasErrors()){
            return "specialization/add-specialization";
        }
        specializationService.add(specialization);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(Specialization specialization, Model model) {
        return "specialization/add-specialization";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Specialization specialization = specializationService.getById(id);
        model.addAttribute("specialization", specialization);
        return "specialization/update-specialization";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid Specialization specialization,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        if (result.hasErrors()) {
            specialization.setId(id);
            return "specialization/update-specialization";
        }
        specializationService.update(specialization);
        Page<Specialization> page = specializationService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "specialization/index-specialization";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,  @PageableDefault(size = 10, sort="name") Pageable pageable) {
        Specialization specialization = specializationService.getById(id);
        specializationService.delete(specialization);
        Page<Specialization> page = specializationService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "specialization/index-specialization";
    }

    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="name") Pageable pageable,  Specialization specialization){
        Page<Specialization> page = specializationService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "specialization/index-specialization";
    }

    private Model sort(Page<Specialization> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}

