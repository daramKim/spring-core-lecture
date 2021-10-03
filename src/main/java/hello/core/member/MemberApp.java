package hello.core.member;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member memberA = new Member(1L, "memberA", Grade.VIP);
        memberService.join(memberA);

        Member member  = memberService.findMember(1L);
        System.out.println("memberA = " + memberA.getName());
        System.out.println("member = " + member.getName());
    }
}
