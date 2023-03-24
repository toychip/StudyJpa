package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.Memberservice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody 두개를 합쳐서 RestController로 적음
// 데이터 자체를 json이나 api로 바로 보낼때 @ResponseBody를 사용함
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final Memberservice memberservice;

    @GetMapping("/api/v1/members")  // 응답 값으로 엔티티를 직접 외부에 노출
    // 엔티티의 모든 값이 노출된다.
    // 실제 현업에서 개발할때 이렇게 개발하면 안된다. 엔티티 수정시 스팩이 변경되며 오류가 날 수 있고 유지보수가 매우 어렵다.
    public List<Member> membersV1(){
        return memberservice.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberservice.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());  // ListMember 를 dto로 변환
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;  // 이렇게 원하는 데이터를 직접 추가해서 할 수 있음 아래와 같이
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;        // 노출하고 싶은 것만 노출함 api스펙이 dto와 코드가 1대1이된다. 필요한것만 노출해서 좋다.
    }



    //등록이므로 postMapping
    @PostMapping("/api/v1/members")     // 회원을 등록하는 api
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        //RequestBody -> Json으로 온 Body를 member에 매핑해준다.
        //  json 데이터를 멤버로 바꿔준다.
        Long id = memberservice.join(member);
        return new CreateMemberResponse(id);
        // 여기서 문제는 어떠한 방식으로 회원가입할지 알 수 없으니 엔티티를 파라미터로 넣으면 안된다. dto를 사용해야하는 이유.
    }


    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.name);

        Long id = memberservice.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        //수정할때는 변경감지 사용하기
        memberservice.update(id, request.getName());
//        /query 와 command를 분리하여 개발한다. 개발한다 이렇게 할경우 추후에 유지보수가 비교적 쉬워진다.
        Member findMember = memberservice.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
        //response dto 반
    }



    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty   // 이것이 가능 V1과는 다르게
        private String name;

    }
    @Data       // 응답값
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
