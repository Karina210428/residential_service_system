package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.model.Request;

import web.model.Specialization;
import web.model.User;
import web.service.RequestService;
import web.service.UserService;

import java.util.List;
/**
 * контроллер для обработки запросов на заявки
 */
@RestController
@RequestMapping("/api/request/")
public class RequestApiController {

    /**
     * сервис
     */
    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    /**
     * добавление заявки
     */
    @RequestMapping(value = "add",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Request> add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Request request){
        User user = userService.findUserByUsername(userDetails.getUsername());
        if(user.getOccupant()!=null){
            request.setOccupant(user.getOccupant());
        }else if(user.getEmployee()!=null){
            request.setEmployee(user.getEmployee());
        }
        requestService.add(request);
        return requestService.getAll();
    }

    /**
     * изменения заявок
     */
    @RequestMapping(value = "update/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public Request updateById(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Request request,
                              @PathVariable int id) {
        return requestService.update(id,request);
    }

    @RequestMapping(value = "update",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public void update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Request request) {
        requestService.update(request);
    }

    @RequestMapping(value = "updateEmployee/{id}",
            method = RequestMethod.PUT,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public Request updateRequestEmployee(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Request request,
                                 @PathVariable int id) {
        User user = userService.findUserByUsername(userDetails.getUsername());
        request.setEmployee(user.getEmployee());
        requestService.update(request);
        return requestService.getById(id);
    }

    /**
     * удаление заявки
     */
    @RequestMapping(value = "delete/{id}",
            method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        requestService.deleteById(id);
    }

    /**
     * получить все заявки
     */
    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Request> getAll(@AuthenticationPrincipal UserDetails userDetails){
        return requestService.getAll();
    }

    @RequestMapping(value = "getAllById/{id}",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Request> getAllByIdEmployee(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable int id){
        User user = userService.findUserByUsername(userDetails.getUsername());
        List<Request> requests = null;
        if(user.getRoles().stream().findFirst().get().getRole().equals("OCCUPANT")){
            requests =  requestService.getAllByIdOccupant(user.getOccupant().getId());
        }else if(user.getRoles().stream().findFirst().get().getRole().equals("EMPLOYEE")) {
            requests = requestService.getAllByIdEmployee(user.getEmployee().getId());
        }
        return requests;
    }

    @RequestMapping(value = "getAllByType",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<Request> getAllByType(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findUserByUsername(userDetails.getUsername());
        List<Request> requests = null;
        if(user.getRoles().stream().findFirst().get().getRole().equals("EMPLOYEE")) {
            requests = requestService.getAllByType(user.getEmployee().getSpecialization());
        }else if(user.getRoles().stream().findFirst().get().getRole().equals("GLOBAL ADMIN")){
            requests = requestService.getAllTypeByAdmin();
        }
        return requests;
    }
}