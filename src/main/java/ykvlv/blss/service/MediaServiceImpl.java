package ykvlv.blss.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ykvlv.blss.data.dto.mapper.MediaMapper;
import ykvlv.blss.data.dto.request.MediaRequest;
import ykvlv.blss.data.dto.response.MediaResponse;
import ykvlv.blss.data.entity.Media;
import ykvlv.blss.data.repository.MediaRepository;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @NonNull
    @Override
    public MediaResponse create(@NonNull MediaRequest mediaRequest) {
        Media media = mediaMapper.map(mediaRequest);
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
    public MediaResponse update(@NonNull Long id, @NonNull MediaRequest mediaRequest) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new BEWrapper(BusinessException.MEDIA_NOT_FOUND, id));

        mediaMapper.map(media, mediaRequest);

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
