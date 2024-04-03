package ykvlv.blss.posterservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ykvlv.blss.commons.result.ImageProcessResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PosterProcessingService {

	@Value("${processed.image.response.queue.name}")
	private String processedImageResponseQueue;

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@JmsListener(destination = "${image.processing.request.queue.name}")
	public void processPoster(Message message) throws JMSException, IOException {
		byte[] bytes = message.getBody(byte[].class);
		ImageProcessResult request = objectMapper.readValue(bytes, ImageProcessResult.class);

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
		ImageIO.write(croppedImage, "png", baos);
		byte[] processedImageData = baos.toByteArray();

		// Отправка обработанного изображения обратно
		var processedImageResult = new ImageProcessResult(request.getUuid(), processedImageData);
		rabbitTemplate.convertAndSend(processedImageResponseQueue, objectMapper.writeValueAsString(processedImageResult));
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

		// Эффект тени
		g2d.setColor(Color.BLUE);
		g2d.drawString(text, x + 5, y + 5);

		// Эффект тени
		g2d.setColor(Color.RED);
		g2d.drawString(text, x - 5, y - 5);

		// Основной текст
		g2d.setColor(Color.WHITE);
		g2d.drawString(text, x, y);

		return g2d;
	}

}
