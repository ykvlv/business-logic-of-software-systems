package ykvlv.blss.application.task.register;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.domain.dto.request.ClientDTO;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidateUserTask implements JavaDelegate {

	private final Validator validator;

	@Override
	public void execute(DelegateExecution execution) {
		var login = (String) execution.getVariable("login");
		var password = (String) execution.getVariable("password");
		var name = (String) execution.getVariable("name");

		var clientDTO = new ClientDTO();
		clientDTO.setLogin(login);
		clientDTO.setPassword(password);
		clientDTO.setName(name);

		var violations = validator.validate(clientDTO);

		if (!violations.isEmpty()) {
			var validationErrors = violations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.joining(", "));
			throw new BEWrapper(BusinessException.VALIDATION_ERROR, validationErrors);
		}

		execution.setVariable("clientDTO", clientDTO);
	}
}
