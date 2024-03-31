package ykvlv.blss.povarenok.data.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum RoleEnum {
	USER(Set.of(PrivilegeEnum.LIKE, PrivilegeEnum.COOKBOOK, PrivilegeEnum.CREATOR, PrivilegeEnum.REVIEWER)),
	ADMIN(Set.of(PrivilegeEnum.COOKBOOK, PrivilegeEnum.MAINTAINER));

	private final Set<PrivilegeEnum> authorities;

}
