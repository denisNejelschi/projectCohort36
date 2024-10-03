package gr36.clubActiv.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "activities")
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
  private  String title;

  @Column(name = "address")
  private  String address;

  @Column(name = "date-time")
  private LocalDate startDate;

  @Column(name = "image")
  private String image;

  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user-activities",
      joinColumns = @JoinColumn(name = "activity_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @JsonIgnore
  private List<User> users;

  public void addUser(User user) {
    users.add(user);
  }

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

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
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
    if (!(o instanceof Activity activity)) {
      return false;
    }
    return Objects.equals(getId(), activity.getId()) && Objects.equals(getTitle(),
        activity.getTitle()) && Objects.equals(getAddress(), activity.getAddress())
        && Objects.equals(getStartDate(), activity.getStartDate())
        && Objects.equals(getImage(), activity.getImage()) && Objects.equals(
        getDescription(), activity.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getAddress(), getStartDate(), getImage(),
        getDescription());
  }

  @Override
  public String toString() {
    return String.format("Activity: id - %d, title - %s, address - %s, startDate - %s, image - %s,"
            + "description - %s",
             id, title, address, startDate, image, description);
  }
}