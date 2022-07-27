package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void joinTest() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush(); // insert 쿼리문 확인
        assertThat(member).isEqualTo(memberRepository.findById(savedId));
    }

    @Test
    void joinDuplicateMemberTest() throws Exception {
        //givin
        Member memberA = new Member();
        Member memberB = new Member();

        memberA.setName("kim");
        memberB.setName("kim");

        //when
        memberService.join(memberA);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(memberB);
        });
    }
}