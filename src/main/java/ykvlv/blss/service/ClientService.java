package ykvlv.blss.service;

import lombok.NonNull;
import ykvlv.blss.data.dto.request.ClientDTO;
import ykvlv.blss.data.dto.response.ClientResponse;
import ykvlv.blss.data.dto.response.SearchMediasResponse;

public interface ClientService {

	@NonNull
	ClientResponse create(@NonNull ClientDTO clientDTO);

	@NonNull
	ClientResponse read(@NonNull String login);

	@NonNull
	ClientResponse update(@NonNull String login, @NonNull ClientDTO clientDTO);

	void delete(@NonNull String login);

	boolean toggleFavoriteMedia(@NonNull String login, @NonNull Long mediaId);

	@NonNull
	SearchMediasResponse getFavoriteMedias(@NonNull String login);

}
