package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.News;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.NewsService;
import gr36.clubActiv.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

  private final NewsService newsService;
  private final UserService userService;

  public NewsController(NewsService newsService, UserService userService) {
    this.newsService = newsService;
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<News> createNews(@RequestBody News news, Authentication authentication) {
    String username = authentication.getName();
    User creator = userService.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    news.setCreatedBy(creator);
    News createdNews = newsService.create(news);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
  }
}
