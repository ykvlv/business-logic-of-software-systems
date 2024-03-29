package ykvlv.blss.service;

import lombok.NonNull;
import ykvlv.blss.data.dto.request.ReviewDTO;
import ykvlv.blss.data.dto.response.ReviewResponse;

public interface ReviewService {

	@NonNull
	ReviewResponse create(@NonNull ReviewDTO reviewDTO, String login);

	@NonNull
	ReviewResponse read(@NonNull Long id);

	@NonNull
	ReviewResponse update(@NonNull Long id, @NonNull ReviewDTO reviewDTO);

	boolean isEligibleTo(@NonNull Long reviewId, @NonNull String login);

	void delete(@NonNull Long id);
}
