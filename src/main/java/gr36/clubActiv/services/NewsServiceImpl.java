package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.News;
import gr36.clubActiv.repository.NewsRepository;
import gr36.clubActiv.services.interfaces.NewsService;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {

  private final NewsRepository newsRepository;

  public NewsServiceImpl(NewsRepository newsRepository) {
    this.newsRepository = newsRepository;
  }

  @Override
  public News create(News news) {
    return newsRepository.save(news);
  }
}
