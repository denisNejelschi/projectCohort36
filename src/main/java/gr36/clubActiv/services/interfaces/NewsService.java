package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.dto.NewsDto;
import gr36.clubActiv.domain.entity.News;

import java.util.List;
import java.util.Optional;

public interface NewsService {

  News create(News news);

  List<News> findAll();

  Optional<News> findById(Long id);

  News update(Long id, NewsDto newsDto);

  void delete(Long id);
}
