package ykvlv.blss.povarenok.data.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ykvlv.blss.povarenok.data.dto.request.ClientDTO;
import ykvlv.blss.povarenok.data.dto.response.ClientResponse;
import ykvlv.blss.povarenok.data.entity.Client;
import ykvlv.blss.povarenok.data.type.RoleEnum;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

	public PasswordEncoder passwordEncoder;

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", expression = "java(this.passwordEncoder.encode(dto.getPassword()))")
	@Mapping(target = "roles", expression = "java(java.util.Set.of(role))")
	@Mapping(target = "cookbook", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract Client map(ClientDTO dto, RoleEnum role);

	public abstract ClientResponse map(Client entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", expression = "java(this.passwordEncoder.encode(dto.getPassword()))")
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "cookbook", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract void map(@MappingTarget Client entity, ClientDTO dto);

}
