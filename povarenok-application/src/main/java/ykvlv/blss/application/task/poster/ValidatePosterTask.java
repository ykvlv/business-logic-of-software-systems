package ykvlv.blss.application.task.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.commons.result.ImageProcessResult;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidatePosterTask implements JavaDelegate {

	private static final String POSTER_EXTENSION = ".jpg";

	private final PovarenokProperties properties;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

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

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		var base64String = (String) execution.getVariable("base64String");

		byte[] decodedBytes = Base64.getDecoder().decode(base64String);

		byte[] jpegSignature = new byte[] {(byte) 0xFF, (byte) 0xD8};
		if (!Arrays.equals(Arrays.copyOf(decodedBytes, 2), jpegSignature)) {
			throw new BEWrapper(BusinessException.POSTER_FILE_TYPE_NOT_SUPPORTED);
		}

		String uuid = UUID.randomUUID().toString();

		// Копируем дефолтный файл в директорию с новым UUID в имени
		Path defaultFilePath = Paths.get(properties.getDefaultFilePath());
		Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());
		Path targetFilePath = posterDirectoryPath.resolve(uuid + POSTER_EXTENSION);
		Files.copy(defaultFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);

		// Отправляем запрос на обработку изображения
		var imageProcessResult = new ImageProcessResult(uuid, decodedBytes);
		rabbitTemplate.convertAndSend(properties.getProcessingQueueName(), objectMapper.writeValueAsString(imageProcessResult));
	}
}
