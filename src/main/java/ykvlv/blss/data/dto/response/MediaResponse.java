package ykvlv.blss.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;

import java.util.Set;

@Data
@NoArgsConstructor
public class MediaResponse {
	@NonNull
	private Long id;

	@NonNull
	private String title;

	private String description;

	@NonNull
	private Long duration;

	private String posterUUID;

	@NonNull
	private MediaTypeEnum mediaTypeEnum;

	private Set<GenreEnum> genreEnums;

}
