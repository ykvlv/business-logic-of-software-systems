package ykvlv.blss.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ykvlv.blss.data.dto.mapper.ReviewMapper;
import ykvlv.blss.data.dto.request.ReviewDTO;
import ykvlv.blss.data.dto.response.ReviewResponse;
import ykvlv.blss.data.entity.Review;
import ykvlv.blss.data.repository.ReviewRepository;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewMapper reviewMapper;

	@NonNull
	@Override
	public ReviewResponse create(@NonNull ReviewDTO reviewDTO) {
		Review review = reviewMapper.map(reviewDTO);
		review = reviewRepository.save(review);

		return reviewMapper.map(review);
	}

	@NonNull
	@Override
	@Transactional(readOnly = true) // Для автоматической инициализации
	public ReviewResponse read(@NonNull Long id) {
		Review review = reviewRepository.findById(id)
				.orElseThrow(() -> new BEWrapper(BusinessException.REVIEW_NOT_FOUND, id));

		return reviewMapper.map(review);
	}

	@NonNull
	@Override
	@Transactional // Для автоматической инициализации
	public ReviewResponse update(@NonNull Long id, @NonNull ReviewDTO reviewDTO) {
		Review review = reviewRepository.findById(id)
				.orElseThrow(() -> new BEWrapper(BusinessException.REVIEW_NOT_FOUND, id));

		reviewMapper.map(review, reviewDTO);

		review = reviewRepository.save(review);

		return reviewMapper.map(review);
	}

	@Override
	public void delete(@NonNull Long id) {
		if (!reviewRepository.existsById(id)) {
			throw new BEWrapper(BusinessException.REVIEW_NOT_FOUND, id);
		}

		reviewRepository.deleteById(id);
	}

}
