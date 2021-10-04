package hello.core.member;

import java.util.HashMap;

public class MemoryMemberRepository implements MemberRepository{

    // 메모리 저장소 생성
    private static HashMap<Long, Member> store = new HashMap<>();
    
    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }
}
