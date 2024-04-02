package ykvlv.blss.domain.repository;

import org.springframework.stereotype.Repository;
import ykvlv.blss.domain.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
