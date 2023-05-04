package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;
import JPA.SpringDataJpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
