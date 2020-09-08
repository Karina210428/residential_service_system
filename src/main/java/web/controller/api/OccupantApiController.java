package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.Occupant;
import web.service.OccupantService;

import java.util.List;

@RestController
@RequestMapping("/api/occupant/")
public class OccupantApiController {

    @Autowired
    private OccupantService occupantService;

    @RequestMapping(value = "add", //
            method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Occupant> add(@RequestBody Occupant occupant){
        occupantService.add(occupant);
        return occupantService.getAll();
    }

    @RequestMapping(value = "update/{id}", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public Occupant update(@RequestBody Occupant occupant, @PathVariable int id) {
        return occupantService.update(id,occupant);
    }

    @RequestMapping(value = "delete/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id) {
        occupantService.deleteById(id);
        return ResponseEntity.ok().body("Occupant has been deleted successfully.");
    }

    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Occupant> getAll(){
        return occupantService.getAll();
    }
}