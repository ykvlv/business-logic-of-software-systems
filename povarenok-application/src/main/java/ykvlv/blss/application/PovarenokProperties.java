package ykvlv.blss.application;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "blss.povarenok")
public class PovarenokProperties {
	public static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	public static final String DEFAULT_PAGE_SIZE = "10";
	public static final String DEFAULT_PAGE_NUMBER = "0";

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
}
