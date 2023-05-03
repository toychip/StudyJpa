package JPA.SpringDataJpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})   // 연관관계 field는 ToString 금지 무한루프 가능성
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // 관례적으로 테이블명_id
    private Long id;
    private String username;

    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")   // fk 명을 잡음
    private Team team;
    // 연관관계 편의 메서드

    public void changeTeam(Team team) {

        this.team = team; // 팀 변경 가능
        team.getMembers().add(this); // 팀을 바꿨으니 팀에 소속된 member도 추가를 해줘야함
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @Builder
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if (team == null){  // exception!
            System.out.println("원래 여기선 exception 안만듬 아직");
        } else {
            changeTeam(team);
        }
    }

    public Member(String username) {
        this.username = username;
    }

    //    protected Member() {}    // 프록시 기술 등 때문에 표준 스펙이 protected를 정하라고 명시.
}
