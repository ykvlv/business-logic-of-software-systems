package ykvlv.blss;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "blss")
public class BLSSProperties {
	public static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	public static final String DEFAULT_PAGE_SIZE = "10";
	public static final String DEFAULT_PAGE_NUMBER = "0";

	/**
	 * Путь к директории для сохранения файлов.
	 */
	@NonNull
	private String posterDirectory;

}
