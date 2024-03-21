package ykvlv.blss.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ykvlv.blss.data.repository.ClientRepository;
import ykvlv.blss.data.entity.Client;
import ykvlv.blss.data.type.PrivilegeEnum;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final ClientRepository clientRepository;

	@Override
	@Transactional // Для автоматической инициализации
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));

		var roles = client.getRoles().stream()
				.map(Enum::toString)
				.toList()
				.toArray(new String[0]);

		List<PrivilegeEnum> authorities = client.getRoles().stream()
				.flatMap(role -> role.getAuthorities().stream())
				.distinct()
				.collect(Collectors.toList());

		return User.builder()
				.username(client.getLogin())
				.password(client.getPassword())
				.roles(roles)
				.authorities(authorities)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
