package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.model.Specialization;
import web.service.SpecializationService;

import java.util.List;

@RestController
@RequestMapping("/api/specialization/")
public class SpecializationController {

    @Autowired
    private SpecializationService service;

    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Specialization> getAll(){
        return service.getAll();
    }
}