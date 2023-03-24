package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany         // 다대다 매핑을 할 경우 추후에 수정 불가능하므로 실전에서는 사용 불가능
    @JoinTable(name = "category_item",
    joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)  // 내 부모니까 ManyToOne이다.
    @JoinColumn(name = "parent_id")
    private Category parent;
    // ^
    // ㅜ 둘이 매핑
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // ==== 연관관계 편의 메서드 ==== //
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
    // ======================== //

}
