package ykvlv.blss.data.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;
import ykvlv.blss.data.validator.NullOrNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class SearchMediasDTO {

	@NullOrNotBlank(message = "Заголовок может отсутствовать но не должен быть пустым")
	private String title;

	@Min(value = 1, message = "Продолжительность должна быть положительной")
	@Max(value = 31536000000L, message = "Продолжительность должна быть не больше 31536000000 миллисекунд, ок?")
	private Long durationFrom;

	@Min(value = 1, message = "Продолжительность должна быть положительной")
	@Max(value = 31536000000L, message = "Продолжительность должна быть не больше 31536000000 миллисекунд, ок?")
	private Long durationTo;

//	TODO
//	private Boolean favorite;

	private MediaTypeEnum mediaTypeEnum;

	private Set<GenreEnum> genreEnums;

	@Valid @NotNull(message = "Параметры постраничного поиска должны быть указаны")
	private PagingOptions pagingOptions;

	@Data
	@NoArgsConstructor
	public static class PagingOptions {
		@NotNull(message = "Номер страницы должен быть указан")
		@Min(value = 0, message = "Номер страницы должен быть не менее 0")
		private Integer pageNumber;

		@NotNull(message = "Размер страницы должен быть указан")
		@Min(value = 1, message = "Размер страницы должен быть больше 0")
		@Max(value = 100, message = "Размер страницы должен быть не более 100")
		private Integer pageSize;

		@Valid
		private List<SortingOptions> sortingOptions = new ArrayList<>();

		@Data
		@NoArgsConstructor
		public static class SortingOptions {
			@NullOrNotBlank(message = "Имя атрибута сортировки может отсутствовать но не должно быть пустым")
			private String attributeName;
			private Boolean descending;
		}

	}

}
