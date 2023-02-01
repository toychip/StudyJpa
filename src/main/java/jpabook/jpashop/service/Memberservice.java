package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)      // 데이터를 변경하는 것은 트랜잭션이 꼭 있어야한다,
public class Memberservice {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    // 회원 가입
    public Long join(Member member){
        validateDuplicateMember(member);    // 중복 회원 검증

        memberRepository.save(member);  // 문제가 없다면 정상적으로 저장
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 중복회원이면 오류를 터뜨릴것이다.
        List<Member> findMembers = memberRepository.findByName(member.getName());// 같은 이름을 가진 회원이 있는지
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 한 명의 회원만 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
