package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;

/**
 * Mapper for the EventsModel
 */
@Mapper(componentModel = "spring")
public interface EventsMapper {
  
  EventsDto toDto(EventsModel eventsModel);

  @Mapping(target = "id", ignore = true)
  EventsModel toModel(EventsPostDto eventsDto);

  List<EventsDto> toDtoList(List<EventsModel> eventsModelList);
}
