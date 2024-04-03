package ykvlv.blss.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ykvlv.blss.domain.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByLogin(String login);

	boolean existsByLogin(String login);

	void deleteByLogin(String login);

	@Query(value = "SELECT COUNT(*) > 0 FROM cookbook " +
			"WHERE client_id = :clientId AND recipe_id = :recipeId", nativeQuery = true)
	boolean checkIfRecipeInCookbook(@Param("clientId") Long clientId, @Param("recipeId") Long recipeId);

	@Modifying
	@Query(value = "INSERT INTO cookbook (client_id, recipe_id) " +
			"VALUES (:clientId, :recipeId)", nativeQuery = true)
	void addToCookbook(@Param("clientId") Long clientId, @Param("recipeId") Long recipeId);

	@Modifying
	@Query(value = "DELETE FROM cookbook " +
			"WHERE client_id = :clientId AND recipe_id = :recipeId", nativeQuery = true)
	void removeFromCookbook(@Param("clientId") Long clientId, @Param("recipeId") Long recipeId);

	@Query(value = "SELECT COUNT(*) > 0 FROM likes " +
			"WHERE client_id = :clientId AND recipe_id = :recipeId", nativeQuery = true)
	boolean checkIfRecipeLiked(@Param("clientId") Long clientId, @Param("recipeId") Long recipeId);

	@Modifying
	@Query(value = "INSERT INTO likes (client_id, recipe_id) " +
			"VALUES (:clientId, :recipeId)", nativeQuery = true)
	void likeRecipe(@Param("clientId") Long clientId, @Param("recipeId") Long recipeId);

}
