package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.configuration.jwt.JwtTokenProvider;
import web.configuration.jwt.UserDetailsServiceImpl;
import web.model.*;
import web.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/auth")
public class LoginApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private OccupantService occupantService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "signin", //
            method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody AuthenticationRequest data) {
        try {
            User user = userService.findUserByUsername(data.getUsername());
            String username = data.getUsername();
            List<String> roles = userService.findUserByUsername(username).getRoles().stream().map(i->i.getRole()).collect(toList());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(),data.getPassword(), user.getAuthorities()));
            String token = jwtTokenProvider.createToken(username,roles);
            Map<Object, Object> model = new HashMap<>();
            model.put("user_id",user.getId());
            model.put("username", username);
            model.put("token", token);
            model.put("role", roles.get(0));
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @RequestMapping(value = "me", //
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public User currentUser(@AuthenticationPrincipal UserDetails userDetails){
        return userService.findUserByUsername(userDetails.getUsername());
    }

    /*
        регистрация пользователя
     */
    @RequestMapping(value = "registrationOccupant", //
            method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public User createNewOccupant(@RequestBody User user) {
        return userService.saveOccupantUser(user);
    }

    @RequestMapping(value = "registrationEmployee", //
            method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public User createNewEmployee(@RequestBody User user) {
        return userService.saveEmployeeUser(user);
    }

    @RequestMapping(value = "initDB", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String initBD() {

        User admin = new User();
        admin.setUsername("admin@gmail.com");
        admin.setPassword("12345678");
        admin.setEnabled(1);
        userService.saveUser(admin);

        for(int i=0;i<5;i++){
            User user = new User();
            user.setUsername("user" +i+"@gmail.com");
            user.setPassword("12345678");
            if(i%2==0){
                Apartment apartment = new Apartment();
                apartment.setBlock(i);
                apartment.setNumber(i);
                apartment.setFloor(i);
                apartmentService.add(apartment);

                Occupant occupant = new Occupant();
                occupant.setBirthday(LocalDate.of(1999, 04,21));
                occupant.setName("Name" + i);
                occupant.setPatronymic("Patronymic" + i);
                occupant.setPhone("+37533123456" + i);
                occupant.setEmail(user.getUsername());
                occupant.setSurname("Surname" + i);
                occupant.setApartment(apartment);
                occupantService.add(occupant);

                user.setOccupant(occupant);
                userService.saveOccupantUser(user);

                Request request = new Request();
                request.setDateOfRequest(LocalDate.now());
                request.setProblem("Problem" + i);
                request.setOccupant(occupant);
                request.setIsDone(false);
               // request.set
                requestService.add(request);

                //request.setType("Заведующий");
                requestService.add(request);
            }else {
                Employee employee = new Employee();
                employee.setName("Name" + i);
                employee.setEmail(user.getUsername());
                employee.setPatronymic("Patronymic" + i);
                employee.setPhone("+37533123456" + i);
                employee.setBirthday(LocalDate.of(1999, 04,21));
               // employee.setSpecialization("Сантехник");
                employee.setSurname("Surname" + i);
                employeeService.add(employee);

                user.setEmployee(employee);
                userService.saveEmployeeUser(user);
            }

            News news = new News();
            news.setHeading("Heading №" + i);
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < new Random().nextInt(100); j++) {
                stringBuilder.append("Text " + j);
            }
            news.setText(stringBuilder.toString());
            news.setPublicationDate(LocalDateTime.now());
            newsService.add(news);
        }
        return "is init";
    }
}
