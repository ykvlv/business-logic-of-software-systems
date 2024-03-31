package ykvlv.blss.data.dto.request.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import ykvlv.blss.data.validator.NullOrNotBlank;

@Data
@NoArgsConstructor
public class SortingOptionsDTO {

	@NullOrNotBlank(message = "Имя атрибута сортировки может отсутствовать но не должно быть пустым")
	private String attributeName;

	private Boolean descending;

}
