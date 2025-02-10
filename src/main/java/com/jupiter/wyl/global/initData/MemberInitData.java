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

                // 가데이터 입력 종료

                if (memberService.findByEmail("apple@aaa.aaa").isEmpty()) {
                    Member member1 = memberService.join("apple@aaa.aaa", "김호러", "123456");
                    Member member2 = memberService.join("banana@aaa.aaa", "이사랑", "123456");
                    Member member3 = memberService.join("cherry@aaa.aaa", "최웃음", "123456");

                    member1.setLikeGenres("공포,미스터리,스릴러");
                    member1.setLikeKeywords("cold,based on novel or book,gothic horror,desire,satire,aging,celebrity");

                    member2.setLikeGenres("가족,모험,드라마,애니메이션");
                    member2.setLikeKeywords("witch,dancing,based on novel or book,college,bangkok,thailand,remake,italian");

                    member3.setLikeGenres("코미디,액션,SF");
                    member3.setLikeKeywords("moon,sequel,based on video game,holiday,kidnapping,santa claus,polar bear,christmas");
                }
            }
        };
    }
}
