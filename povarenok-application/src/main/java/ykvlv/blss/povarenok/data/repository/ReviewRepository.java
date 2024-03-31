package ykvlv.blss.povarenok.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ykvlv.blss.povarenok.data.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
