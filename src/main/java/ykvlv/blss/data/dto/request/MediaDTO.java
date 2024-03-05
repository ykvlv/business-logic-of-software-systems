package ykvlv.blss.data.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;

import java.util.Set;

@Data
@NoArgsConstructor
public class MediaDTO {

        @NotBlank(message = "Название должно присутствовать")
        @Size(min = 3, message = "Название должно быть не менее 3 символов")
        @Size(max = 100, message = "Название должно быть не более 100 символов")
        @Pattern(regexp = "^\\S.*\\S$", message = "Название не должно начинаться или заканчиваться пробельным символом")
        @Pattern(regexp = "^(\\S+\\s?)+$", message = "Название не должно содержать пробельные символы идущие подряд")
        private String title;

        @Size(min = 10, message = "Описание может отсутствовать или должно быть не менее 10 символов")
        @Size(max = 1000, message = "Описание может отсутствовать или должно быть не более 1000 символов")
        @Pattern(regexp = "^\\S.*\\S$", message = "Описание не должно начинаться или заканчиваться пробельным символом")
        @Pattern(regexp = "^(\\S+\\s?)+$", message = "Описание не должно содержать пробельные символы идущие подряд")
        private String description;

        @NotNull(message = "Продолжительность должна быть указана")
        @Min(value = 1, message = "Продолжительность должна быть положительной")
        @Max(value = 31536000000L, message = "Продолжительность должна быть не больше 31536000000 миллисекунд, ок?")
        private Long duration;

        @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
                message = "UUID постера может отсутствовать или должен быть в формате UUID v4")
        private String posterUUID;

        @NotNull(message = "Тип медиа должен присутствовать")
        private MediaTypeEnum mediaTypeEnum;

        private Set<GenreEnum> genreEnums;

}
