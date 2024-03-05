package ykvlv.blss.data.repository;

import org.springframework.stereotype.Repository;
import ykvlv.blss.data.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

}
