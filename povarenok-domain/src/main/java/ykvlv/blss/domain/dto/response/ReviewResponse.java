package ykvlv.blss.domain.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ReviewResponse {

	@NonNull
	private Long id;

	@NonNull
	private Long recipeId;

	@NonNull
	private String login;

	@NonNull
	private LocalDateTime creationDate;

	@NonNull
	private Short score;

	@NonNull
	private String text;

	private String posterUUID;

}
