package ykvlv.blss.domain.dto.request.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.domain.validator.NullOrNotBlank;

@Data
@NoArgsConstructor
public class SortingOptionsDTO {

	@NullOrNotBlank(message = "Имя атрибута сортировки может отсутствовать но не должно быть пустым")
	private String attributeName;

	private Boolean descending;

}
