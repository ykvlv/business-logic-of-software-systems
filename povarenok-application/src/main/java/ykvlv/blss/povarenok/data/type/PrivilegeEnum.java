package ykvlv.blss.povarenok.data.type;


import org.springframework.security.core.GrantedAuthority;

public enum PrivilegeEnum implements GrantedAuthority {
	LIKE,		// Возможность лайкать
	COOKBOOK,	// Возможность вести кулинарную книгу
	REVIEWER,	// Возможность оставлять отзывы
	CREATOR,	// Возможность создавать рецепты
	MAINTAINER;	// Возможность обслуживать сервис

	@Override
	public String getAuthority() {
		return this.name();
	}

}
