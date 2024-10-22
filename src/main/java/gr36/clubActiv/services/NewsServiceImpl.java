package gr36.clubActiv.services;

import gr36.clubActiv.domain.dto.NewsDto;
import gr36.clubActiv.domain.entity.News;
import gr36.clubActiv.repository.NewsRepository;
import gr36.clubActiv.services.interfaces.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  @Override
  public List<News> findAll() {
    return newsRepository.findAll();
  }

  @Override
  public Optional<News> findById(Long id) {
    return newsRepository.findById(id);
  }

  @Override
  public News update(Long id, NewsDto newsDto) {
    News existingNews = newsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("News not found"));
    if (newsDto.getTitle() != null) {
      existingNews.setTitle(newsDto.getTitle());
    }
    if (newsDto.getDescription() != null) {
      existingNews.setDescription(newsDto.getDescription());
    }

    return newsRepository.save(existingNews);
  }


  @Override
  public void delete(Long id) {
    News news = newsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("News not found"));

    newsRepository.delete(news);
  }
}
