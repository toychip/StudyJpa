//package hellojpa;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@Entity//(name = "Member") // 아래 클래스와 동일한 이름을 사용한다. 생략하면 기본 값 사용
//@SequenceGenerator(name = "member_seq_generator",
//sequenceName = "member_seq")
////@Table(name = "Member", uniqueConstraints = {@UniqueConstraint(
////        name = "NAME_AGE_UNIQUE",
////        columnNames = {"NAME", "AGE"})})
////
//
////            1. @Column(columnDenfinition = "varchar(100) default 'EMPTY'") 데이터 베이스 컬럼 정보를 직접 줄 수 있음.
////            테이블 매핑 이름 설정(테이블 이름을 바꾸고 싶을때), from 절 바꾼다고 생각하면된다.
////            2. @Column(precision = n)
////            아주 큰 숫자나 소수점을 정할때 사용
////            3. @Enumerated(EnumType.STRING)
////            private RoleType roletype;        EnumType=ORDINAL 사용 금지. 나중에 추가,
////            수정했을때 enum의 숫자 혼동있으니 필수로 string 사용, 순서대로 들어가서 or
//
//public class Member {
//
////    @Column(insertable = false)   -> column을 수정했을때 db에 반영할거냐 말거냐 기본이 트루임
////    @Column(updatable = false)
//// @Column(name = "NAME", nullable = false, length = 10)
//// 객체 이름은 name, DB 스카마 이름은 NAME으로 설정, null값 불가, not null로 설정, 길이 10자 초과 제한
//// nullable 을 false로 하면 not null 제약조건이 걸린다.
//
////    4. LocalDate와 LocalDateTime
////    private LocalDate 년도, 월 데이터 타입
////    private LocalDateTime 년도, 월, 일 데이터 타입
//
//    @Id
//    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
//    private Long id;
//
//    @Column(name = "NAME", nullable = false)    //, length = 10
//    // 객체 이름은 name, DB 스카마 이름은 NAME으로 설정, null값 불가, not null로 설정, 길이 10자 초과 제한
//    // nullable 을 false로 하면 not null 제약조건이 걸린다.
//    private String name;
//
//    private Integer age;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    //    1월 13일 추가
//// --   @Enumerated(EnumType.STRING)    //DB에는 ENUM 타입이 없는 db가 많음. 그때 에노테이션 사용
//////    DB는 관례적으로 언더스코어를 주로 사용한다. @Colmun(name = "")를 사용한다.
//// --   private RoleType roleType;
////    자바의 enum을 사용해서 회원의 타입을 구분. 일반 회원은 USER, 관리자는 ADMIN으로 구분
////    자바의 enum을 사용하려면 @Enumerated 어노테이션으로 매핑을 꼭 해야한다.
//
//// --   @Temporal(TemporalType.TIMESTAMP)
//// --   private Date createdDate;   // 생성일자
//
////    createdDate, lastModifiedDate: 자바의 날짜 타입은 @Temporal()을 사용하여 매핑한다.
////   -- @Temporal(TemporalType.TIMESTAMP)
////   -- private Date lastModifiedDate;    // 수정일자
//
////  --  @Lob
//// --   private String description;
////    db에 VARCHAR의 크기를 넘어서는 데이터를 저장하고 싶을때 @Lob를 사용
////    회원을 설명하는 필드는 길이 제한이 없다. 따라서 DB의 VARCHAR 타입 대신에 CLOD타입으로 저장해야한다.
////    @Lob를 사용하면 CLOD, BLOB 타입을 매핑할 수 있다.
////    5. @Lob은 지정할 수 있는 속성이 없음. 매핑하는 필드 타입이 문자면 CLOB, 나머지는 BLOB로 매핑한다.
//
//
////    @Transient
////    private int temp;
//
////    db에 넣고 싶지 않고 메모리에서만 계산하고 싶을때 Transient 사용
//
//}
