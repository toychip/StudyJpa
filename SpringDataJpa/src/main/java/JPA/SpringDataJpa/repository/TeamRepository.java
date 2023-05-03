package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Member, Long> {
}
