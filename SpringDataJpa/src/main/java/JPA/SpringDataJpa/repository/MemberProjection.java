package JPA.SpringDataJpa.repository;

public interface MemberProjection {
    // member의 id, Username, Team이름 가져오는 쿼리

    Long getId();

    String getUsername();

    String getTeamName();
}
