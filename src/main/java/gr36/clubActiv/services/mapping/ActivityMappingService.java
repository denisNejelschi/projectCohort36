package gr36.clubActiv.services.mapping;


import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ActivityMappingService {

  @Mapping(source = "author.id", target = "authorId")
  ActivityDto mapEntityToDto(Activity entity);

  @Mapping(target = "author.id", source = "authorId")
  Activity mapDtoToEntity(ActivityDto dto);
}
