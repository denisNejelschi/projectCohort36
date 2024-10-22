package gr36.clubActiv.controller;


import gr36.clubActiv.domain.entity.Review;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.ReviewNotFounException;
import gr36.clubActiv.services.interfaces.ReviewService;
import gr36.clubActiv.services.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

  private final ReviewService reviewService;
  private final UserService userService;

  public ReviewController(ReviewService reviewService, UserService userService) {
    this.reviewService = reviewService;
    this.userService = userService;
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping()
  public ResponseEntity<Review> createReview(@RequestBody Review review,
      Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    review.setCreatedBy(username);
    Review savedReview = reviewService.saveReview(review);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteReview(@PathVariable Long id, Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Review review = reviewService.findById(id)
            .orElseThrow(() -> new ReviewNotFounException("Review not found"));

    boolean isAuthor = review.getCreatedBy().equals(username);
    boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAuthor && !isAdmin) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body("You are not authorized to delete this review.");
    }
    reviewService.deleteReview(id);
    return ResponseEntity.ok("Review deleted successfully");
  }

  @PreAuthorize("hasRole('USER')")
  @PutMapping("/{id}")
  public ResponseEntity<String> updateReview(@PathVariable Long id, @RequestBody Review updatedReview, Authentication authentication) {
    String username = authentication.getName();

    Review existingReview = reviewService.findById(id)
            .orElseThrow(() -> new ReviewNotFounException("Review with ID " + id + " not found"));

    // Проверяем, что текущий пользователь - создатель отзыва
    if (!existingReview.getCreatedBy().equals(username)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body("You are not authorized to update this review.");
    }

    // Обновляем только переданные поля
    if (updatedReview.getTitle() != null) {
      existingReview.setTitle(updatedReview.getTitle());
    }
    if (updatedReview.getDescription() != null) {
      existingReview.setDescription(updatedReview.getDescription());
    }
    if (updatedReview.getRating() != 0) {  // Проверяем, чтобы рейтинг был больше 0
      existingReview.setRating(updatedReview.getRating());
    }

    // Обновляем поле created_at автоматически
    existingReview.setCreatedAt(LocalDateTime.now());

    reviewService.update(existingReview);

    return ResponseEntity.ok("Review updated successfully");
  }


  @GetMapping()
  public ResponseEntity<List<Review>> getReviews() {
    List<Review> reviews = reviewService.getAllReviews();
    return ResponseEntity.ok(reviews);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
    Review review = reviewService.findById(id)
            .orElseThrow(() -> new ReviewNotFounException("Review with ID " + id + " not found"));
    return ResponseEntity.ok(review);
  }
}
