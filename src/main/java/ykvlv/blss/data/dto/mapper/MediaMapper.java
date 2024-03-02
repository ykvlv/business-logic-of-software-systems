package ykvlv.blss.data.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ykvlv.blss.data.dto.request.MediaRequest;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.entity.Media;


@Mapper(componentModel = "spring")
public abstract class MediaMapper {

	public abstract Media map(MediaRequest request);

	public abstract MediaResponse map(Media media);

	public abstract void map(@MappingTarget Media entity, MediaRequest request);

}
