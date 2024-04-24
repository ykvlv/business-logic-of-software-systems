package ykvlv.blss.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ykvlv.blss.commons.PasswordUtils;
import ykvlv.blss.domain.entity.Client;
import ykvlv.blss.domain.type.RoleEnum;
import ykvlv.blss.domain.dto.request.ClientDTO;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", expression = "java(hashPassword(dto.getPassword(), salt))")
	@Mapping(target = "roles", expression = "java(java.util.Set.of(role))")
	@Mapping(target = "cookbook", ignore = true)
	@Mapping(target = "likes", ignore = true)
	public abstract Client map(ClientDTO dto, RoleEnum role, String salt);

	public String hashPassword(String password, String salt) {
		return PasswordUtils.hashPassword(password, salt);
	}

}
