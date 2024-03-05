package ykvlv.blss.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchMediasResponse {

	@NonNull
	private PagingResult pagingResult;

	@NonNull
	private List<MediaResponse> medias;

	@Data
	@Builder
	@AllArgsConstructor
	public static class PagingResult {

		@NonNull
		private Integer pageNumber;

		@NonNull
		private Integer pageSize;

		@NonNull
		private Boolean morePagesAvailable;

	}

}
