package web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.model.News;
import web.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news/")
public class NewsApiController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "add", //
            method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<News> add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody News news){
        newsService.add(news);
        return newsService.getAll();
    }

    @RequestMapping(value = "update/{id}", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public News update(@RequestBody News news, @PathVariable int id) {
        return newsService.update(id,news);
    }

    @RequestMapping(value = "delete/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id) {
        newsService.deleteById(id);
        return ResponseEntity.ok().body("News has been deleted successfully.");
    }

    @RequestMapping(value = "getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<News> getAll(@AuthenticationPrincipal UserDetails userDetails)
    {
        return newsService.getAll();

    }

}
