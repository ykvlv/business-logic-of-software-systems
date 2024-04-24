package ykvlv.blss.application.task;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.commons.PasswordUtils;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;
import ykvlv.blss.domain.repository.ClientRepository;

@Component
@RequiredArgsConstructor
public class AuthUserTask implements JavaDelegate {

	private final ClientRepository clientRepository;

	@Override
	public void execute(DelegateExecution execution) {
		var login = (String) execution.getVariable("login");
		var password = (String) execution.getVariable("password");

		var client = clientRepository.findByLogin(login).orElseThrow(
				() -> new BEWrapper(BusinessException.CLIENT_NOT_FOUND, login));

		var hashedPassword = PasswordUtils.hashPassword(password, client.getSalt());

		if (!hashedPassword.equals(client.getPassword())) {
			throw new BEWrapper(BusinessException.INVALID_PASSWORD, login);
		}

		execution.setVariable("client", client);
	}
}
