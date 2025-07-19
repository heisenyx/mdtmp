package dev.heisen.metadata.mapper;

import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.model.Publication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicationMapper {
    PublicationMetadataResponse toResponse(Publication publication);
}
