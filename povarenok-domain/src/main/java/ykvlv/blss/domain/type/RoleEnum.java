package ykvlv.blss.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum RoleEnum {
	USER(Set.of(PrivilegeEnum.LIKE, PrivilegeEnum.CREATOR)),
	ADMIN(Set.of(PrivilegeEnum.MAINTAINER));

	private final Set<PrivilegeEnum> authorities;

}
