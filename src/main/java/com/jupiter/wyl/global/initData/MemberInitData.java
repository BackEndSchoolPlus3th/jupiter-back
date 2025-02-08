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

                if (memberService.findByEmail("apple@aaa.aaa") == null) {
                    Member member1 = memberService.join("apple@aaa.aaa", "김호러", "123456");
                    Member member2 = memberService.join("banana@aaa.aaa", "이사랑", "123456");
                    Member member3 = memberService.join("cherry@aaa.aaa", "최웃음", "123456");

                    //가데이터 입력
                    member1.setLikeGenres("공포,스릴러,로맨스");
                    member1.setLikeKeywords("없음");

                    member1.setLikeGenres("로맨스,드라마,모험,코미디");
                    member1.setLikeKeywords("없음");

                    member1.setLikeGenres("액션,가족,애니메이션");
                    member1.setLikeKeywords("없음");
                }
            }
        };
    }
}
