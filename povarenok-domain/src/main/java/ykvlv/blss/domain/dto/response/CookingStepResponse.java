package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CookingStepResponse {

	@NonNull
	private Long id;

	@NonNull
	private Integer orderNumber;

	@NonNull
	private String description;

}
