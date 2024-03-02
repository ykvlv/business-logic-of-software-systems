package ykvlv.blss.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Repository;
import ykvlv.blss.data.dto.request.SearchMediasDTO;
import ykvlv.blss.data.entity.Media;
import ykvlv.blss.data.entity.Media_;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.exception.BEWrapper;
import ykvlv.blss.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	public Slice<Media> searchMedias(SearchMediasDTO searchMediasDTO) {
		CriteriaQuery<Media> cq = createCriteriaQuery(searchMediasDTO);

		var pagingOptions = searchMediasDTO.getPagingOptions();
		var pageRequest = PageRequest.of(pagingOptions.getPageNumber(), pagingOptions.getPageSize());

		TypedQuery<Media> query = entityManager.createQuery(cq)
				.setFirstResult(PageableUtils.getOffsetAsInteger(pageRequest))
				.setMaxResults(pageRequest.getPageSize() + 1); // на 1 больше, чтобы узнать есть ли следующая страница

		List<Media> medias = query.getResultList();
		boolean hasNext = medias.size() > pageRequest.getPageSize();

		return new SliceImpl<>(hasNext ? medias.subList(0, pageRequest.getPageSize()) : medias, pageRequest, hasNext);
	}

	private CriteriaQuery<Media> createCriteriaQuery(SearchMediasDTO searchMediasDTO) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Media> cq = builder.createQuery(Media.class);

//		cq.distinct(true);

		var predicates = new ArrayList<Predicate>();
		var root = cq.from(Media.class);

		var title = searchMediasDTO.getTitle();
		if (title != null) {
			predicates.add(builder.like(root.get(Media_.title), "%" + title.trim() + "%"));
		}

		var mediaTypeEnum = searchMediasDTO.getMediaTypeEnum();
		if (mediaTypeEnum != null) {
			predicates.add(builder.equal(root.get(Media_.mediaTypeEnum), mediaTypeEnum));
		}

		var durationFrom = searchMediasDTO.getDurationFrom();
		if (durationFrom != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Media_.duration), durationFrom));
		}

		var durationTo = searchMediasDTO.getDurationTo();
		if (durationTo != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Media_.duration), durationTo));
		}

		if (durationFrom != null && durationTo != null && durationFrom > durationTo) {
			throw new BEWrapper(BusinessException.INVALID_RANGE, Media_.duration);
		}

		var genreEnums = searchMediasDTO.getGenreEnums();
		if (genreEnums != null && !genreEnums.isEmpty()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			for (GenreEnum genre : genreEnums) {
				predicates.add(cb.isMember(genre, root.get(Media_.genreEnums)));
			}
		}

		cq.where(predicates.toArray(Predicate[]::new));

		// Параметры сортировки
		List<Order> orders = new ArrayList<>();
		var sortingOptions = searchMediasDTO.getPagingOptions().getSortingOptions();

		for (int i = 0; i < sortingOptions.size(); i++) {
			var sortingOption = sortingOptions.get(i);
			boolean isDescending = Objects.requireNonNullElse(sortingOption.getDescending(), false);
			String attributeName = sortingOption.getAttributeName().trim();

			try {
				var path = root.get(attributeName);

				orders.add(isDescending ? builder.desc(path) : builder.asc(path));
			} catch (IllegalArgumentException e) {
				throw new BEWrapper(BusinessException.INVALID_SORTING_ATTRIBUTE,
						String.format("pagingOptions.sortingOptions[%s].attributeName", i),
						attributeName);
			}
		}
		cq.orderBy(orders);

		return cq;
	}
}

