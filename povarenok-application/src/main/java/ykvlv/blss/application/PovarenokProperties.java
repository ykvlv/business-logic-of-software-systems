package ykvlv.blss.application;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "blss.povarenok")
public class PovarenokProperties {

	/**
	 * Путь к директории для сохранения файлов.
	 */
	@NonNull
	private String posterDirectory;

	/**
	 * Расположение имени файла который будет использоваться по умолчанию.
	 */
	@NonNull
	private String defaultFilePath;

	/**
	 * Количество дней, после которых удаляются записи аналитики.
	 */
	@NonNull
	private Short deleteAnalyticsRecordsOlderThanDays;

	/**
	 * Название очереди для обработки постеров.
	 */
	@NonNull
	private String processingQueueName;

	/**
	 * Название очереди для принятия обработанных постеров.
	 */
	@NonNull
	private String processedQueueName;
}
