package ykvlv.blss.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ykvlv.blss.data.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
