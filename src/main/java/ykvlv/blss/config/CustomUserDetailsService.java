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

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final ClientRepository clientRepository;

	@Override
	@Transactional // Для автоматической инициализации
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Client client = clientRepository.findByLogin(login)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));

		String[] roles = client.getRoles().stream()
				.map(Enum::toString)
				.toList()
				.toArray(new String[0]);

		return User.builder()
				.username(client.getLogin())
				.password(client.getPassword())
				.roles(roles)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
