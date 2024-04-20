package ykvlv.blss.application.task.poster;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.commons.result.ImageProcessResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProcessPosterTask implements JavaDelegate {

	private final PovarenokProperties properties;
	private final RuntimeService runtimeService;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@JmsListener(destination = "${blss.povarenok.processing-queue-name}")
	public void processPoster(Message message) throws JMSException, IOException {
		byte[] bytes = message.getBody(byte[].class);
		ImageProcessResult request = objectMapper.readValue(bytes, ImageProcessResult.class);

		// Запуск задачи по обработке изображения
		Map<String, Object> processVariables = new HashMap<>();
		processVariables.put("request", request);
		runtimeService.startProcessInstanceByMessage("ProcessPosterMessage", processVariables);
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		var request = (ImageProcessResult) execution.getVariable("request");

		ByteArrayInputStream bais = new ByteArrayInputStream(request.getImageData());
		BufferedImage originalImage = ImageIO.read(bais);

		// Кадрирование до квадрата
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int newHW = Math.min(width, height);
		BufferedImage croppedImage = originalImage.getSubimage((width - newHW) / 2, (height - newHW) / 2, newHW, newHW);

		// Добавление водяного знака
		Graphics2D g2d = getGraphics2D(croppedImage, newHW);
		g2d.dispose();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(croppedImage, "jpg", baos);
		byte[] processedImageData = baos.toByteArray();

		// Отправка обработанного изображения обратно
		var processedImageResult = new ImageProcessResult(request.getUuid(), processedImageData);
		rabbitTemplate.convertAndSend(properties.getProcessedQueueName(), objectMapper.writeValueAsString(processedImageResult));
	}

	private static Graphics2D getGraphics2D(BufferedImage image, int newHW) {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		String text = "ГОВНОКОД";

		// Настройка сглаживания и качества рендеринга
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		Font font = new Font("Arial", Font.BOLD, 10); // Начальный размер шрифта
		FontMetrics fontMetrics = g2d.getFontMetrics(font);
		int textWidth = fontMetrics.stringWidth(text);

		// Рассчитываем размер шрифта для заполнения почти всей ширины изображения
		while (textWidth < newHW) {
			font = font.deriveFont(font.getSize2D() + 1f); // Увеличиваем размер шрифта
			fontMetrics = g2d.getFontMetrics(font);
			textWidth = fontMetrics.stringWidth(text);
		}

		// Устанавливаем итоговый шрифт
		g2d.setFont(font);

		// Рассчитываем позицию текста для центрирования
		int textHeight = fontMetrics.getHeight();
		int x = (newHW - textWidth) / 2;
		int y = (newHW - textHeight) / 2 + fontMetrics.getAscent();

		// Основной текст
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, x, y);

		return g2d;
	}
}
