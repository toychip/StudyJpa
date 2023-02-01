package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 이것이 있어야 롤백 가능
public class MemberserviceTest {

    @Autowired Memberservice memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false)        // 롤백 안하고 commit을 함
    public void 회원가입() throws Exception {
    //given
        Member member = new Member();
        member.setName("first");
    //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
        //저장하려는 회원이 join 함수를 거친 후에 오류 없이 되는지 확인
    }

    @Test()
    public void 중복_회원_예외() throws Exception {

        assertThrows(IllegalStateException.class, () -> {
            // given
            Member member1 = new Member();
            member1.setName("kim1");

            Member member2 = new Member();
            member2.setName("kim1");

            // when
            memberService.join(member1);
            memberService.join(member2);


            // then
            fail("예외가 발생해야 한다.");
        });
    }
}