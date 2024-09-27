package gr36.clubActiv.services.mapping;


import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ActivityMappingService {

  @Mapping(target = "id", ignore = true)
  Activity mapDtoToEntity(ActivityDto dto);
  ActivityDto mapEntityToDto(Activity entity);

}