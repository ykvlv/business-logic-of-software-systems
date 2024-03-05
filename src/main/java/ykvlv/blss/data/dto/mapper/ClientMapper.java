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
	public abstract Client map(ClientDTO request);

	public abstract ClientResponse map(Client client);

	@Mapping(target = "id", ignore = true)
	public abstract void map(@MappingTarget Client entity, ClientDTO request);

}
