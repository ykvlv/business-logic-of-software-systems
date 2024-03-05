package ykvlv.blss.data.dto.response;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
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
