package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.Date;

@Entity//(name = "Member") // 아래 클래스와 동일한 이름을 사용한다. 생략하면 기본 값 사용
@Table(name = "Member", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME", "AGE"}
)})        // 테이블 매핑 이름 설정(테이블 이름을 바꾸고 싶을때), from 절 바꾼다고 생각하면된다.
public class Member {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false, length = 10)
    // 스카마 이름은 NAME, null값 불가, not null로 설정, 길이 10자 초과 제한
    private String name;
    private Integer age;

//    1월 13일 추가
    @Enumerated(EnumType.STRING)
//    DB는 관례적으로 언더스코어를 주로 사용한다. @Colmun(name = "")를 사용한다.
    private RoleType roleType;
//    자바의 enum을 사용해서 회원의 타입을 구분. 일반 회원은 USER, 관리자는 ADMIN으로 구분
//    자바의 enum을 사용하려면 @Enumerated 어노테이션으로 매핑을 꼭 해야한다.

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

//    createdDate, lastModifiedDate: 자바의 날짜 타입은 @Temporal()을 사용하여 매핑한다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;
//    회원을 설명하는 필드는 길이 제한이 없다. 따라서 DB의 VARCHAR 타입 대신에 CLOD타입으로 저장해야한다.
//    @Lob를 사용하면 CLOD, BLOB 타입을 매핑할 수 있다.

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
