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
import web.model.Occupant;
import web.service.ApartmentService;
import web.service.OccupantService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/occupant/")
public class OccupantViewController {

    @Autowired
    private OccupantService occupantService;

    @Autowired
    private ApartmentService apartmentService;

    @PostMapping("add")
    public String add(@Valid Occupant occupant, BindingResult result, Model model){
        if(result.hasErrors()){
            return "occupant/add-occupant";
        }
        occupantService.add(occupant);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(Occupant occupant) {
        return "occupant/add-occupant";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Occupant occupant = occupantService.getById(id);
        model.addAttribute("apartments", apartmentService.getAll());
        model.addAttribute("occupant", occupant);
        return "occupant/update-occupant";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid Occupant occupant,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort="surname") Pageable pageable) {
//        if(occupant.getEmail()==null && occupant.getPassword()==null){
//            occupant.setEmail(occupantService.getById(id).getEmail());
//            occupant.setPassword(occupantService.getById(id).getPassword());
//        }
        if (result.hasErrors()) {
            occupant.setId(id);
            return "occupant/update-occupant";
        }
        // occupant.setApartment(apartmentService.getAll().stream().filter(i->i.));
        occupantService.update(occupant);
        Page<Occupant> page = occupantService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "occupant/index-occupant";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,
                         @PageableDefault(size = 10, sort="surname") Pageable pageable) {
        Occupant occupant = occupantService.getById(id);
        occupantService.delete(occupant);
        Page<Occupant> page = occupantService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "occupant/index-occupant";
    }

    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="surname") Pageable pageable, Occupant occupant){
        Page<Occupant> page = occupantService.findAllPaginated(pageable);
        model = sort(page,model);
        model.addAttribute("page", page);
        return "occupant/index-occupant";
    }

    @GetMapping("moderation/{id}")
    public String moderation(@PathVariable("id") int id, Model model,
                             @PageableDefault(size = 10, sort = "surname") Pageable pageable) {
        Occupant occupant = occupantService.getById(id);
      //  occupant.setModeration(1);
        occupantService.update(occupant);
        Page<Occupant> page = occupantService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "occupant/index-occupant";
    }
    private Model sort(Page<Occupant> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}
