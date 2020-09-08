package web.controller.view;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import web.model.User;
import web.model.VerificationToken;
import web.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.stream.Collectors;

/*
 * контроллер для обработки запросов, связанные с пользователями
 */
@Controller
@RequestMapping("/user/")
public class UserViewController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;
    /**
     * добавление пользователя
     */
    @PostMapping("add")
    public String add(@Valid User user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "user/add-user";
        }
        userService.add(user);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(User user) {
        return "user/add-user";
    }

    /**
     * форма ввода для редактирования пользователя
     */
    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user/update-user";
    }

    /**
     * редактирование пользователя
     */
    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid User user,
                             BindingResult result, Model model,
                             @PageableDefault(size = 10) Pageable pageable) {
        if (result.hasErrors()) {
            user.setId(id);
            return "user/update-user";
        }
        userService.update(user);
        Page<User> page = userService.findAllPaginated(pageable);
        model.addAttribute("page", page);
        return "user/index-user";
    }

    /**
     * удаление пользователя
     */
    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,
                             @PageableDefault(size = 10, sort="username") Pageable pageable) {
        User user = userService.getById(id);
        userService.delete(user);
        Page<User> page = userService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "user/index-user";
    }

    /**
     * подтверждение пользователя (модерация)
     */
    @GetMapping("moderation/{id}")
    public String moderation(@PathVariable("id") int id, Model model,
                             @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        User user = userService.getById(id);
        user.setEnabled(1);
        userService.update(user);
        Page<User> page = userService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "user/index-user";
    }

    /**
     * список всех пользователей
     */
    @GetMapping("list")
    public String index(Model model, @PageableDefault(size = 10, sort="username") Pageable pageable, User user){
        Page<User> page = userService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "user/index-user";
    }

    private Model sort(Page<User> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, Model model,
                                      @RequestParam("token") String token){
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
        user.setEnabled(1);
        userService.saveUser(user);
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }
}
