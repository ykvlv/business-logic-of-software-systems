package ykvlv.blss.data.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;

import java.util.Set;

@Data
@NoArgsConstructor
public class MediaResponse {
	private Long id;
	private String title;
	private String description;
	private Long duration;
	private String posterUUID;
	private MediaTypeEnum mediaTypeEnum;
	private Set<GenreEnum> genreEnums;
}
