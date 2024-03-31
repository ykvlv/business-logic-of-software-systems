package ykvlv.blss.povarenok.data.repository;

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
import ykvlv.blss.povarenok.data.dto.request.search.SearchRecipesDTO;
import ykvlv.blss.povarenok.data.entity.Recipe;
import ykvlv.blss.povarenok.data.entity.Recipe_;
import ykvlv.blss.povarenok.data.type.TagEnum;
import ykvlv.blss.povarenok.exception.BEWrapper;
import ykvlv.blss.povarenok.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

	@PersistenceContext
	private final EntityManager entityManager;

	public Slice<Recipe> searchRecipes(SearchRecipesDTO searchRecipesDTO) {
		CriteriaQuery<Recipe> cq = createCriteriaQuery(searchRecipesDTO);

		var pagingOptions = searchRecipesDTO.getPagingOptionsDTO();
		var pageRequest = PageRequest.of(pagingOptions.getPageNumber(), pagingOptions.getPageSize());

		TypedQuery<Recipe> query = entityManager.createQuery(cq)
				.setFirstResult(PageableUtils.getOffsetAsInteger(pageRequest))
				.setMaxResults(pageRequest.getPageSize() + 1); // на 1 больше, чтобы узнать есть ли следующая страница

		List<Recipe> recipes = query.getResultList();
		boolean hasNext = recipes.size() > pageRequest.getPageSize();

		return new SliceImpl<>(hasNext ? recipes.subList(0, pageRequest.getPageSize()) : recipes, pageRequest, hasNext);
	}

	private CriteriaQuery<Recipe> createCriteriaQuery(SearchRecipesDTO searchRecipesDTO) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> cq = builder.createQuery(Recipe.class);

//		cq.distinct(true);

		var predicates = new ArrayList<Predicate>();
		var root = cq.from(Recipe.class);

		var title = searchRecipesDTO.getTitle();
		if (title != null) {
			predicates.add(builder.like(root.get(Recipe_.title), "%" + title.trim() + "%"));
		}

		var recipeTypeEnum = searchRecipesDTO.getRecipeTypeEnum();
		if (recipeTypeEnum != null) {
			predicates.add(builder.equal(root.get(Recipe_.recipeTypeEnum), recipeTypeEnum));
		}

		var tagEnums = searchRecipesDTO.getTagEnums();
		if (tagEnums != null && !tagEnums.isEmpty()) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			for (TagEnum tag : tagEnums) {
				predicates.add(cb.isMember(tag, root.get(Recipe_.tagEnums)));
			}
		}

		cq.where(predicates.toArray(Predicate[]::new));

		// Параметры сортировки
		List<Order> orders = new ArrayList<>();
		var sortingOptions = searchRecipesDTO.getPagingOptionsDTO().getSortingOptionDTOs();

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
