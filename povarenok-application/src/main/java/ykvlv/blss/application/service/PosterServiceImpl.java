package ykvlv.blss.application.service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.domain.dto.result.PosterReadResult;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {
	private final static String POSTER_CONTENT_TYPE = "image/png";
	private final static String POSTER_EXTENSION = ".png";

	private final PovarenokProperties properties;

	@PostConstruct
	public void initPosterDirectory() {
		try {
			Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());

			// Проверка наличия директории для загрузки постеров, иначе создаем её
			if (!Files.exists(posterDirectoryPath)) {
				Files.createDirectories(posterDirectoryPath);
			}
		} catch (IOException e) {
			throw new RuntimeException("Ошибка при создании директории для постеров", e);
		}
	}

	@NonNull
	@Override
	public List<String> all(int page, int size) {
		Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());

		int startIndex = page * size;
		int endIndex = Math.min(startIndex + size, Integer.MAX_VALUE);

		try (Stream<Path> posterFiles = Files.list(posterDirectoryPath)) {
			return posterFiles
					.filter(path -> {
						try {
							return Files.isRegularFile(path) && Files.probeContentType(path).equals(POSTER_CONTENT_TYPE);
						} catch (IOException e) {
							log.warn("Ошибка при получении типа файла", e);

							return false;
						}
					})
					.sorted()
					.skip(startIndex)
					.limit(endIndex)
					.map(path -> path.getFileName().toString().replace(POSTER_EXTENSION, ""))
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Ошибка при получении списка постеров", e);
		}
	}

	@NonNull
	@Override
	public String create(@NonNull MultipartFile file) {
		try {
			Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());

			// Проверяем, что загруженный файл имеет формат PNG
			if (POSTER_CONTENT_TYPE.equals(file.getContentType())) {
				String uuid = UUID.randomUUID().toString();
				String fileName = uuid + POSTER_EXTENSION;

				Path filePath = posterDirectoryPath.resolve(fileName);

				Files.copy(file.getInputStream(), filePath);
				return uuid;
			} else {
				// Если загруженный файл не является PNG, возвращаем ошибку
				throw new BEWrapper(BusinessException.POSTER_FILE_TYPE_NOT_SUPPORTED, file.getContentType());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@NonNull
	@Override
	public PosterReadResult read(@NonNull String uuid) {
		if (!uuid.matches(PovarenokProperties.UUID_REGEX)) {
			return PosterReadResult.notFound();
		}

		Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());
		String fileName = uuid + POSTER_EXTENSION;
		Path filePath = posterDirectoryPath.resolve(fileName);

		if (Files.exists(filePath) && Files.isRegularFile(filePath) && Files.isReadable(filePath)) {
			try {
				return PosterReadResult.success(Files.readAllBytes(filePath));
			} catch (IOException e) {
				return PosterReadResult.error();
			}
		} else {
			return PosterReadResult.notFound();
		}
	}

	@Override
	public void delete(@NonNull String uuid) {
		verifyUUID(uuid);

		try {
			Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());
			String fileName = uuid + POSTER_EXTENSION;
			Path filePath = posterDirectoryPath.resolve(fileName);

			if (Files.exists(filePath) && Files.isRegularFile(filePath) && Files.isReadable(filePath)) {
				Files.delete(filePath);
			} else {
				throw new BEWrapper(BusinessException.POSTER_NOT_FOUND, uuid);
			}
		} catch (IOException e) {
			throw new RuntimeException("Ошибка при удалении постера", e);
		}
	}

	private void verifyUUID(@NonNull String uuid) {
		if (!uuid.matches(PovarenokProperties.UUID_REGEX)) {
			throw new BEWrapper(BusinessException.POSTER_NOT_FOUND, uuid);
		}
	}

}
