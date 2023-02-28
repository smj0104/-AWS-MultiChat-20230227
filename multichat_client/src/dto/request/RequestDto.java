package dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequsetDto<T> {
	private String resource;
	private T body;
}
