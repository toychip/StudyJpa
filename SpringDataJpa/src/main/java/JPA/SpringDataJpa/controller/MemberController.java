package JPA.SpringDataJpa.controller;

import JPA.SpringDataJpa.dto.MemberDto;
import JPA.SpringDataJpa.entity.Member;
import JPA.SpringDataJpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(
            @PageableDefault(size = 5,
            sort = "id",
            direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable); // 엔티티 노출 절대 금지

//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
//        http://localhost:8080/members?page=4&size=3&sort=id,desc
//        http://localhost:8080/members?page=4&size=3&sort=id,desc&sort=username,desc
        return map;
    }

    @PostConstruct
    public void init() {
//        memberRepository.save(new Member("userA"));

        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
