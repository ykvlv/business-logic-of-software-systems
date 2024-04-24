package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class PagingResult implements Serializable {

	@NonNull
	private Integer pageNumber;

	@NonNull
	private Integer pageSize;

	@NonNull
	private Boolean morePagesAvailable;

}
