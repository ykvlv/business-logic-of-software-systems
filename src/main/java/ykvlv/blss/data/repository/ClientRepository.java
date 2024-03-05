package ykvlv.blss.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ykvlv.blss.data.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByLogin(String login);

	boolean existsByLogin(String login);

	void deleteByLogin(String login);

	@Query(value = "SELECT COUNT(*) > 0 FROM clients_favorite_medias " +
			"WHERE client_id = :clientId AND media_id = :mediaId", nativeQuery = true)
	boolean checkIfMediaFavorite(@Param("clientId") Long clientId, @Param("mediaId") Long mediaId);

	@Modifying
	@Query(value = "INSERT INTO clients_favorite_medias (client_id, media_id) " +
			"VALUES (:clientId, :mediaId)", nativeQuery = true)
	void addFavoriteMedia(@Param("clientId") Long clientId, @Param("mediaId") Long mediaId);

	@Modifying
	@Query(value = "DELETE FROM clients_favorite_medias " +
			"WHERE client_id = :clientId AND media_id = :mediaId", nativeQuery = true)
	void removeFavoriteMedia(@Param("clientId") Long clientId, @Param("mediaId") Long mediaId);

}
