package hellojpa;

import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    // 기간 Period 으로 묶고
    private Period workPeriod;

    @Embedded
    // 주소로 묶고 싶음
    private Address homeAddress;



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

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    //    이곳을 FK로 갖고오기
//    @Column(name = "TEAM_ID")
//    private Long teamId;

//    @ManyToOne(fetch = FetchType.LAZY)      // LAZY 프록시로 가져옴, EAGER 즉시 로딩 바로 가져옴 member와 team을 계속 사용한다면
    // @ManyToOne, @OneToOne 둘 다 기본이 즉시로딩으로 설정되어 있으므로 (fetch = FetchType.LAZY) 설정 꼭 해주기
    // EAGER는 쿼리가 너무 많이 나가서 실무에서는 적용을 안하는게 좋다
    // 여러명의 멤버와 하나의 팀으로 매핑하므로 Memo 입장에서는 다, ManyToOne으로 매핑한다.
//    @JoinColumn //(name = "TEAM_ID", insertable = false, updatable = false)
//    private Team team;

//    @OneToOne
//    @JoinColumn(name = "LOCKER_ID")
//    private Locker locker;


//    public Team getTeam() {
//        return team;
//    }
//
//    public void changeTeam(Team team) {
//        this.team = team;
//        team.getMemberTS().add(this);       // jpamain에서 직접하지 말고 setter에서 해결하면 실수를 안할수있음
//    }
//
//    public void setTeam(Team team) {
//        this.team = team;
//    }
}
