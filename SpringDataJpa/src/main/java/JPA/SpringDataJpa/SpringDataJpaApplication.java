package JPA.SpringDataJpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SpringDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		// 여기서 원래 SpringSecurity를 사용하여 세션을 꺼내는 방식을 사용함
		return () -> Optional.of(UUID.randomUUID().toString());	// Interface에서 메서드가 1개면 람다고 바꿀 수 있음
	}
}
