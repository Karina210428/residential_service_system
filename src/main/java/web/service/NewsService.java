package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import web.model.News;
import web.repository.NewsRepository;

import java.util.List;

@Service("newsService")
public class NewsService  implements CrudService<News>{

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public void add(News entity) {
        newsRepository.save(entity);
    }

    @Override
    public void update(News entity) {
        newsRepository.save(entity);
    }

    @Override
    public News update(int id, News entity) {
        return null;
    }

    @Override
    public void delete(News entity) {
        newsRepository.delete(entity);
    }

    @Override
    public void deleteById(int id) {
        newsRepository.deleteById(id);
    }

    @Override
    public News getById(int id) {
        return newsRepository.getOne(id);
    }

    @Override
    public List<News> getAll() {
        return newsRepository.findAll();
    }

    @Override
    public Page<News> findAllPaginated(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }
}
