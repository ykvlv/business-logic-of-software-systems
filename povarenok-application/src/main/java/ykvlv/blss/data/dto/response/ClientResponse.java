package ykvlv.blss.data.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ClientResponse {

	@NonNull
	private Long id;

	@NonNull
	private String login;

	@NonNull
	private String name;

}
