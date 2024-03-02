package ykvlv.blss.data.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ErrorResponse {
	private String message;
	private Timestamp timestamp;

	public ErrorResponse(String message) {
		this.message = message;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

}
