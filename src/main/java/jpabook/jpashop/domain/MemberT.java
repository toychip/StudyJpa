package jpabook.jpashop.domain;


import javax.persistence.*;

@Entity
public class MemberT {

    @Id @GeneratedValue

    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    이곳을 FK로 갖고오기
//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne  // 여러명의 멤버와 하나의 팀으로 매핑하므로 Member 입장에서는 다, ManyToOne으로 매핑한다.
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
