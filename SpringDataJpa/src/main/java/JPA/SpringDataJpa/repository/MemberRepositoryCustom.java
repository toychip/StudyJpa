package JPA.SpringDataJpa.repository;

import JPA.SpringDataJpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
