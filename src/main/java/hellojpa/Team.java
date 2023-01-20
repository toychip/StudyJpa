package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

//    @OneToMany(mappedBy = "team")       // 어떤것과 연결할지를 명시해주는데, team 이라는 객체와 매핑한다고 알려줌
//    private List<Member> member = new ArrayList<>();
    // team은 member와 나는 매핑했었음 착각하지 말기


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

//    public List<Member> getMemberS() {
//        return memberS;
//    }
//
//    public void setMemberTS(List<Member> memberTS) {
//        this.memberTS = memberTS;
//    }
//
//    public void addMemberT(Member memberT) {
//        memberT.setTeam(this);
//        memberTS.add(memberT);
//    }
}
