package ykvlv.blss.service;


import lombok.NonNull;
import ykvlv.blss.data.dto.request.MediaRequest;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.entity.Media;

public interface MediaService {
	@NonNull
	MediaResponse create(@NonNull MediaRequest mediaRequest);

	@NonNull
	MediaResponse read(@NonNull Long id);

	@NonNull
	MediaResponse update(@NonNull Long id, @NonNull MediaRequest mediaRequest);

	void delete(@NonNull Long id);

}
