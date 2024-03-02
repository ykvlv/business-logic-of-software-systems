package ykvlv.blss.data.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ykvlv.blss.data.dto.request.MediaDTO;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.entity.Media;


@Mapper(componentModel = "spring")
public abstract class MediaMapper {

	@Mapping(target = "id", ignore = true)
	public abstract Media map(MediaDTO request);

	public abstract MediaResponse map(Media media);

	@Mapping(target = "id", ignore = true)
	public abstract void map(@MappingTarget Media entity, MediaDTO request);

}
