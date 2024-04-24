package ykvlv.blss.application.task.register;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.commons.PasswordUtils;
import ykvlv.blss.domain.dto.mapper.ClientMapper;
import ykvlv.blss.domain.dto.request.ClientDTO;
import ykvlv.blss.domain.exception.BEWrapper;
import ykvlv.blss.domain.exception.BusinessException;
import ykvlv.blss.domain.repository.ClientRepository;
import ykvlv.blss.domain.type.RoleEnum;

@Component
@RequiredArgsConstructor
public class CreateUserTask implements JavaDelegate {

	private final ClientRepository clientRepository;
	private final ClientMapper clientMapper;

	@Override
	public void execute(DelegateExecution execution) {
		var clientDTO = (ClientDTO) execution.getVariable("clientDTO");

		if (clientRepository.existsByLogin(clientDTO.getLogin())) {
			throw new BEWrapper(BusinessException.CLIENT_ALREADY_EXISTS, clientDTO.getLogin());
		}

		var client = clientMapper.map(clientDTO, RoleEnum.USER, PasswordUtils.getSalt());

		clientRepository.save(client);
	}

}
