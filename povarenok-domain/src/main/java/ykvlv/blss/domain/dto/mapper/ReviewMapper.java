package ykvlv.blss.domain.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ykvlv.blss.domain.entity.Client;
import ykvlv.blss.domain.entity.Recipe;
import ykvlv.blss.domain.entity.Review;
import ykvlv.blss.domain.dto.request.ReviewDTO;
import ykvlv.blss.domain.dto.response.ReviewResponse;
import ykvlv.blss.domain.repository.ClientRepository;
import ykvlv.blss.domain.repository.RecipeRepository;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

	private RecipeRepository recipeRepository;

	private ClientRepository clientRepository;

	@Autowired
	public void setRecipeRepository(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Autowired
	public void setClientRepository(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "recipe", expression = "java(getRecipeById(dto.getRecipeId()))")
	@Mapping(target = "client", expression = "java(getClientByLogin(login))")
	@Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
	public abstract Review map(ReviewDTO dto, String login);

	@Mapping(target = "recipeId", source = "recipe.id")
	@Mapping(target = "login", source = "client.login")
	public abstract ReviewResponse map(Review entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "recipe", ignore = true)
	@Mapping(target = "client", ignore = true)
	@Mapping(target = "creationDate", ignore = true)
	public abstract void map(@MappingTarget Review entity, ReviewDTO dto);

	protected Recipe getRecipeById(Long id) {
		return recipeRepository.findById(id)
				.orElseThrow(() -> new BEWrapper(BusinessException.RECIPE_NOT_FOUND, id));
	}

	protected Client getClientByLogin(String login) {
		return clientRepository.findByLogin(login)
				.orElseThrow(() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));
	}

}
