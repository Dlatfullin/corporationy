package kz.aitu.corporationy.mapper;

import kz.aitu.corporationy.dto.ImageResponse;
import kz.aitu.corporationy.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {

    @Mapping(target = "imageId", source = "id")
    ImageResponse toDto(Image image);

    List<ImageResponse> toDto(List<Image> images);
}