package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import web.model.Apartment;
import web.service.ApartmentService;
import java.util.List;

@RestController
@RequestMapping("/api/apartment/")
public class ApartmentApiController {

    @Autowired
    private ApartmentService apartmentService;

    @RequestMapping(value = "add",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Apartment> add(@RequestBody Apartment apartment){
        apartmentService.add(apartment);
        return apartmentService.getAll();
    }

    @RequestMapping(value = "update/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public Apartment update(@RequestBody Apartment apartment, @PathVariable("id") int id) {
        return apartmentService.update(id, apartment);
    }

    @RequestMapping(value = "delete/{id}",
            method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        apartmentService.deleteById(id);
    }

    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Apartment> getAll(){
        return apartmentService.getAll();
    }
}
