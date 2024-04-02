package ykvlv.blss.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ykvlv.blss.domain.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
