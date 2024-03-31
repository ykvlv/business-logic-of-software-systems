package ykvlv.blss.povarenok.data.repository;

import org.springframework.stereotype.Repository;
import ykvlv.blss.povarenok.data.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
