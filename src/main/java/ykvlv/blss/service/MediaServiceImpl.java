package ykvlv.blss.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import ykvlv.blss.data.dto.mapper.MediaMapper;
import ykvlv.blss.data.dto.request.MediaDTO;
import ykvlv.blss.data.dto.request.SearchMediasDTO;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.dto.response.SearchMediasResponse;
import ykvlv.blss.data.entity.Media;
import ykvlv.blss.data.repository.MediaRepository;
import ykvlv.blss.data.repository.SearchRepository;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final SearchRepository searchRepository;
    private final MediaMapper mediaMapper;

    @NonNull
    @Override
    @Transactional
    public SearchMediasResponse search(@NonNull SearchMediasDTO searchMediasDTO) {
        var slice = searchRepository.searchMedias(searchMediasDTO);

        var medias = slice.getContent().stream()
                .map(mediaMapper::map)
                .toList();

        return SearchMediasResponse.builder()
                .pagingResult(SearchMediasResponse.PagingResult.builder()
                        .pageSize(slice.getSize())
                        .pageNumber(slice.getNumber())
                        .morePagesAvailable(slice.hasNext())
                        .build())
                .medias(medias)
                .build();
    }

    @NonNull
    @Override
    public MediaResponse create(@NonNull MediaDTO mediaDTO) {
        Media media = mediaMapper.map(mediaDTO);
        media = mediaRepository.save(media);

        return mediaMapper.map(media);
    }

    @NonNull
    @Override
    @Transactional
    public MediaResponse read(@NonNull Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new BEWrapper(BusinessException.MEDIA_NOT_FOUND, id));

        return mediaMapper.map(media);
    }

    @NonNull
    @Override
    @Transactional
    public MediaResponse update(@NonNull Long id, @NonNull MediaDTO mediaDTO) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new BEWrapper(BusinessException.MEDIA_NOT_FOUND, id));

        mediaMapper.map(media, mediaDTO);

        media = mediaRepository.save(media);

        return mediaMapper.map(media);
    }

    @Override
    public void delete(@NonNull Long id) {
        if (!mediaRepository.existsById(id)) {
            throw new BEWrapper(BusinessException.MEDIA_NOT_FOUND, id);
        }

        mediaRepository.deleteById(id);
    }

}
