package ykvlv.blss.data.dto.response;

import lombok.Data;
import lombok.NonNull;

@Data
public class ClientResponse {

	@NonNull
	private Long id;

	@NonNull
	private String login;

	@NonNull
	private String name;

}
