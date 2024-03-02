package ykvlv.blss.data.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ErrorResponse {

	@NonNull
	private String message;

	@NonNull
	private Timestamp timestamp;

	public ErrorResponse(@NonNull String message) {
		this.message = message;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

}
