package ykvlv.blss.service;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PosterService {

	@NonNull
	List<String> all(int page, int size);

	@NonNull
	String create(@NonNull MultipartFile file);

	@NonNull
	Optional<byte[]> read(@NonNull String uuid);

	@NonNull
	String update(@NonNull String uuid, @NonNull MultipartFile file);

	void delete(@NonNull String uuid);
}
