package gr36.clubActiv.domain.dto;

import java.time.LocalDate;
import java.util.Objects;


public class ActivityDto {

  private Long id;

  private  String title;

  private  String address;

  private LocalDate startDate;

  private String image;

  private String description;



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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityDto that)) {
      return false;
    }
    return Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(),
        that.getTitle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle());
  }

  @Override
  public String toString() {
    return String.format("Activity: id - %d, title - %s, address - %s, startDate - %s, image - %s, "
            + "description - %s",
        id, title, address, startDate, image, description);
  }
}