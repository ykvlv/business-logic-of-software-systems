package ykvlv.blss.data.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ykvlv.blss.data.dto.request.ClientDTO;
import ykvlv.blss.data.dto.response.ClientResponse;
import ykvlv.blss.data.entity.Client;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cookbook", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract Client map(ClientDTO dto);

	public abstract ClientResponse map(Client entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "cookbook", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract void map(@MappingTarget Client entity, ClientDTO dto);

}
