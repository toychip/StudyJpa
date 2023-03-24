package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)      // 데이터를 변경하는 것은 트랜잭션이 꼭 있어야한다,
//@AllArgsConstructor     // 생성자를 만들어주므로 밑에를 안써도 되는것이다.
@RequiredArgsConstructor    // final 있는 가지고 있는 필드만 가지고 생성자를 만들어준다.
public class Memberservice {

//    @Autowired 이렇게 바로 인젝션을하면 테스트를 하거나 등등 고정되어 있으므로 불편함
    private final MemberRepository memberRepository;  // 밑에 생성자로 자동 연결이기 때문에 변경할 일이 없으므로 final 로 선언해준다.

//    @Autowired  // setter 함수로 가짜 memberRepository를 삽입하여 유동적으로 사용할수있다
//    하지만 누군가 그때 또 만들 수 있으므로 안 좋다.
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    요즘은 생성자 인젝션을 사용한다.

//    RequiredArgsConstructor를 사용하므로써 생략 가능
//    @Autowired  // 생성자로 만들었기 때문에 중간에 set 할 수가 없다.
//    public Memberservice(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @Transactional  // 위 읽기 전용보다 우선권을 가짐
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

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);   // 영속성 컨테스트에서 id를 찾아서 데이터를 긁어올것이다. /영속상태
        member.setName(name);   // 변경감지
    }
}
