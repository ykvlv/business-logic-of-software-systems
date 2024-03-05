package ykvlv.blss.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ykvlv.blss.data.dto.mapper.ClientMapper;
import ykvlv.blss.data.dto.mapper.MediaMapper;
import ykvlv.blss.data.dto.request.ClientDTO;
import ykvlv.blss.data.dto.response.ClientResponse;
import ykvlv.blss.data.dto.response.SearchMediasResponse;
import ykvlv.blss.data.entity.Client;
import ykvlv.blss.data.entity.Media;
import ykvlv.blss.data.repository.ClientRepository;
import ykvlv.blss.data.repository.MediaRepository;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final MediaRepository mediaRepository;
	private final ClientMapper clientMapper;
	private final MediaMapper mediaMapper;

	@NonNull
	@Override
	public ClientResponse create(@NonNull ClientDTO clientDTO) {
		if (clientRepository.existsByLogin(clientDTO.getLogin())) {
			throw new BEWrapper(BusinessException.CLIENT_ALREADY_EXISTS, clientDTO.getLogin());
		}

		Client client = clientMapper.map(clientDTO);

		client = clientRepository.save(client);

		// TODO сохраняем аутентификацию

		return clientMapper.map(client);
	}

	@NonNull
	@Override
	public ClientResponse read(@NonNull String login) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		return clientMapper.map(client);
	}

	@NonNull
	@Override
	public ClientResponse update(@NonNull String login, @NonNull ClientDTO clientDTO) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		if (clientRepository.existsByLogin(clientDTO.getLogin())) {
			throw new BEWrapper(BusinessException.CLIENT_ALREADY_EXISTS, clientDTO.getLogin());
		}

		clientMapper.map(client, clientDTO);

		client = clientRepository.save(client);

		return clientMapper.map(client);
	}

	@Override
	public void delete(@NonNull String login) {
		if (!clientRepository.existsByLogin(login)) {
			throw new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login);
		}

		clientRepository.deleteByLogin(login);
	}

	@Override
	@Transactional
	public boolean toggleFavoriteMedia(@NonNull String login, @NonNull Long mediaId) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		if (!mediaRepository.existsById(mediaId)) {
			throw new BEWrapper(BusinessException.MEDIA_NOT_FOUND, mediaId.toString());
		}

		boolean exists = clientRepository.checkIfMediaFavorite(client.getId(), mediaId);

		// TODO ну типа в многопоточке может быть проблема, но тут не критично
		if (exists) {
			clientRepository.removeFavoriteMedia(client.getId(), mediaId);
		} else {
			clientRepository.addFavoriteMedia(client.getId(), mediaId);
		}

		return !exists;
	}

	@NonNull
	@Override
	public SearchMediasResponse getFavoriteMedias(@NonNull String login) {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		Set<Media> medias = client.getFavoriteMedias();

		// TODO ну тут можно сделать пагинацию, но пока не надо
		return SearchMediasResponse.builder()
				.medias(medias.stream()
						.map(mediaMapper::map)
						.toList())
				.pagingResult(SearchMediasResponse.PagingResult.builder()
						.pageSize(medias.size())
						.pageNumber(0)
						.morePagesAvailable(false)
						.build())
				.build();
	}
	
}
