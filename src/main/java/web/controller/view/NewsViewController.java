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
import web.model.News;
import web.service.NewsService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/news/")
public class NewsViewController {

    @Autowired
    private NewsService newsService;

    @PostMapping("add")
    public String add(@Valid News news, BindingResult result, Model model){
        if(result.hasErrors()){
            return "news/add-news";
        }
        news.setPublicationDate(LocalDateTime.now());
        newsService.add(news);
        return "redirect:list";
    }

    @GetMapping("addForm")
    public String showAddForm(News news, Model model) {
        return "news/add-news";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        News news = newsService.getById(id);
        model.addAttribute("news", news);
        return "news/update-news";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable("id") int id, @Valid News news,
                         BindingResult result, Model model,
                         @PageableDefault(size = 10, sort = "publicationDate") Pageable pageable) {
        if (result.hasErrors()) {
            news.setId(id);
            return "news/update-news";
        }
        news.setPublicationDate(LocalDateTime.now());
        newsService.update(news);
        Page<News> page = newsService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "news/index-news";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model,  @PageableDefault(size = 10, sort="publicationDate") Pageable pageable) {
        News news = newsService.getById(id);
        newsService.delete(news);
        Page<News> page = newsService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "news/index-news";
    }

    @GetMapping("list")
    public String index(Model model , @PageableDefault(size = 10, sort="publicationDate") Pageable pageable,  News news){
        Page<News> page = newsService.findAllPaginated(pageable);
        model = sort(page, model);
        model.addAttribute("page", page);
        return "news/index-news";
    }

    private Model sort(Page<News> page, Model model){
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        return model;
    }
}
