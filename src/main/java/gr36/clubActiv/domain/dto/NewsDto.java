package gr36.clubActiv.domain.dto;

import java.util.Objects;

public class NewsDto {

    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private String createdAt;


    public NewsDto() {}

    public NewsDto(Long id, String title, String description, String createdBy, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsDto newsDto)) return false;
        return Objects.equals(id, newsDto.id) && Objects.equals(title, newsDto.title) && Objects.equals(description, newsDto.description) && Objects.equals(createdBy, newsDto.createdBy) && Objects.equals(createdAt, newsDto.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, createdBy, createdAt);
    }

    @Override
    public String toString() {
        return "NewsDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
