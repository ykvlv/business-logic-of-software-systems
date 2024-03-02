package ykvlv.blss.service;


import lombok.NonNull;
import ykvlv.blss.data.dto.request.MediaDTO;
import ykvlv.blss.data.dto.request.SearchMediasDTO;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.dto.response.SearchMediasResponse;

public interface MediaService {
	@NonNull
	SearchMediasResponse search(@NonNull SearchMediasDTO searchMediasDTO);

	@NonNull
	MediaResponse create(@NonNull MediaDTO mediaDTO);

	@NonNull
	MediaResponse read(@NonNull Long id);

	@NonNull
	MediaResponse update(@NonNull Long id, @NonNull MediaDTO mediaDTO);

	void delete(@NonNull Long id);

}
