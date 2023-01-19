//package hellojpa;
//
//public class ㅁㄴㅇ {
//
//
//            1월 13일 추가
//    @Enumerated(EnumType.STRING)    //DB에는 ENUM 타입이 없는 db가 많음. 그때 에노테이션 사용
////    DB는 관례적으로 언더스코어를 주로 사용한다. @Colmun(name = "")를 사용한다.
//    private RoleType roleType;
//    자바의 enum을 사용해서 회원의 타입을 구분. 일반 회원은 USER, 관리자는 ADMIN으로 구분
//    자바의 enum을 사용하려면 @Enumerated 어노테이션으로 매핑을 꼭 해야한다.
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;   // 생성일자
//
//    createdDate, lastModifiedDate: 자바의 날짜 타입은 @Temporal()을 사용하여 매핑한다.
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;    // 수정일자
//
//    @Lob
//    private String description;
//    db에 VARCHAR의 크기를 넘어서는 데이터를 저장하고 싶을때 @Lob를 사용
//    회원을 설명하는 필드는 길이 제한이 없다. 따라서 DB의 VARCHAR 타입 대신에 CLOD타입으로 저장해야한다.
//    @Lob를 사용하면 CLOD, BLOB 타입을 매핑할 수 있다.
//    5. @Lob은 지정할 수 있는 속성이 없음. 매핑하는 필드 타입이 문자면 CLOB, 나머지는 BLOB로 매핑한다.
//
//
//    @Transient
//    private int temp;
//
//    db에 넣고 싶지 않고 메모리에서만 계산하고 싶을때 Transient 사용
//}
