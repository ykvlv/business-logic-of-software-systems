package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class CookingStepResponse implements Serializable {

	@NonNull
	private Long id;

	@NonNull
	private Integer orderNumber;

	@NonNull
	private String description;

}
