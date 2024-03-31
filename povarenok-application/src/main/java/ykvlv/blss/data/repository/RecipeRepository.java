package ykvlv.blss.data.repository;

import org.springframework.stereotype.Repository;
import ykvlv.blss.data.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
