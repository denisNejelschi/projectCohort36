package gr36.clubActiv.domain.dto;

import gr36.clubActiv.domain.entity.Activity;
import java.time.LocalDate;
import java.util.List;
import gr36.clubActiv.domain.entity.User;

public class ActivityDto {

  private Long id;
  private String title;
  private String address;
  private LocalDate startDate;
  private String image;
  private String description;
  private Long authorId;  // Reference to the ID of the user who created the activity
  private List<Long> userIds;  // List of user IDs participating in the activity

  // Default constructor
  public ActivityDto() {}

  // Constructor that includes all fields
  public ActivityDto(Long id, String title, String address, LocalDate startDate, String image,
      String description, Long authorId, List<Long> userIds) {
    this.id = id;
    this.title = title;
    this.address = address;
    this.startDate = startDate;
    this.image = image;
    this.description = description;
    this.authorId = authorId;
    this.userIds = userIds;
  }

  // Constructor to map from an Activity entity
  public ActivityDto(Activity activity) {
    this.id = activity.getId();
    this.title = activity.getTitle();
    this.address = activity.getAddress();
    this.startDate = activity.getStartDate();
    this.image = activity.getImage();
    this.description = activity.getDescription();
    this.authorId = activity.getAuthor() != null ? activity.getAuthor().getId() : null;
    this.userIds = activity.getUsers() != null ?
        activity.getUsers().stream().map(User::getId).toList() : null;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public void setUserIds(List<Long> userIds) {
    this.userIds = userIds;
  }

  @Override
  public String toString() {
    return "ActivityDto{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", address='" + address + '\'' +
        ", startDate=" + startDate +
        ", image='" + image + '\'' +
        ", description='" + description + '\'' +
        ", authorId=" + authorId +
        ", userIds=" + userIds +
        '}';
  }
}
