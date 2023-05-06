package JPA.SpringDataJpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {
    // 진짜 상속이 아닌 쓸수만 있게 속성만 내려주는 것

    @Column(updatable = false) // db의 값이 변경될 수 없음
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @PrePersist // 최초 등록
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updateDate = now;
    }

    @PreUpdate  // 업데이트시
    public void preUpdate(){
        updateDate = LocalDateTime.now();
    }
}
