package ykvlv.blss.application.task.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.commons.result.ImageProcessResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReceivePosterTask implements JavaDelegate {

	private static final String POSTER_EXTENSION = ".jpg";

	private final RuntimeService runtimeService;
	private final ObjectMapper objectMapper;
	private final PovarenokProperties properties;

	@JmsListener(destination = "${blss.povarenok.processed-queue-name}")
	public void receiveProcessedPoster(Message message) throws JMSException, IOException {
		byte[] bytes = message.getBody(byte[].class);
		ImageProcessResult response = objectMapper.readValue(bytes, ImageProcessResult.class);

		// Запуск задачи по получению изображения
		Map<String, Object> processVariables = new HashMap<>();
		processVariables.put("response", response);
		runtimeService.startProcessInstanceByMessage("ReceivePosterMessage", processVariables);
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		var response = (ImageProcessResult) execution.getVariable("response");

		Path posterDirectoryPath = Paths.get(properties.getPosterDirectory());
		String uuid = response.getUuid(); // Используем UUID из ответа
		String fileName = uuid + POSTER_EXTENSION;
		Path filePath = posterDirectoryPath.resolve(fileName);

		// Сохраняем обработанное изображение в файл
		Files.copy(new ByteArrayInputStream(response.getImageData()), filePath, StandardCopyOption.REPLACE_EXISTING);
	}
}
