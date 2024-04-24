package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class ClientResponse implements Serializable {

	@NonNull
	private Long id;

	@NonNull
	private String login;

	@NonNull
	private String name;

}
