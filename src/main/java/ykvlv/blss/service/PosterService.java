package ykvlv.blss.service;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import ykvlv.blss.data.dto.result.PosterReadResult;

import java.util.List;

public interface PosterService {

	@NonNull
	List<String> all(int page, int size);

	@NonNull
	String create(@NonNull MultipartFile file);

	@NonNull
	PosterReadResult read(@NonNull String uuid);

	void delete(@NonNull String uuid);
}
