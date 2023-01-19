//package hellojpa;
//
//import javax.persistence.*;
//
//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
////          SINGLE_TABLE, JOINED 둘 중에 선택해야함. TABLE_PER_CLASS 사용 xx
//@DiscriminatorColumn  // (name = "DIS_TYPE")
//public abstract class ItemT {
//
//    @Id @GeneratedValue
//    private Long id;
//    private String name;
//    private int price;
//
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
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//}
