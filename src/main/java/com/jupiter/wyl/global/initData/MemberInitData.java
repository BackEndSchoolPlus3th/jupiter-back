package com.jupiter.wyl.global.initData;


import com.jupiter.wyl.domain.member.entity.Member;
import com.jupiter.wyl.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("!prod")
public class MemberInitData {
    @Bean
    public ApplicationRunner applicationRunner(
            MemberService memberService
    ) {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Member member1;
                Member member2;
                Member member3;
                // 가데이터 입력 종료

                if (memberService.findByEmail("apple@aaa.aaa").isEmpty()) {
                    member1 = memberService.join("apple@aaa.aaa", "김호러", "123456");
                    member2 = memberService.join("banana@aaa.aaa", "이사랑", "123456");
                    member3 = memberService.join("cherry@aaa.aaa", "최웃음", "123456");
                }else {
                    member1 = memberService.findByEmail("apple@aaa.aaa").get();
                    member2 = memberService.findByEmail("banana@aaa.aaa").get();
                    member3 = memberService.findByEmail("cherry@aaa.aaa").get();
                }
                    member1.setLikeGenres("공포,미스터리,스릴러");
                    member1.setLikeKeywords("based on novel or book,college,vampire,satire,aging,celebrity");

                    member2.setLikeGenres("가족,모험,드라마,애니메이션");
                    member2.setLikeKeywords("witch,christmas,castle,college,bangkok,thailand,remake,italian");

                    member3.setLikeGenres("코미디,액션,SF");
                    member3.setLikeKeywords("spy,space,science fiction,holiday,kidnapping,santa claus,polar bear,christmas");

            }
        };
    }
}
