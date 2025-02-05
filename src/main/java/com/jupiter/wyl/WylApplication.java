package com.jupiter.wyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = {
		"com.jupiter.wyl.domain.member.repository",
		"com.jupiter.wyl.domain.movie.movie.repository.jpa",
		"com.jupiter.wyl.domain.main.repository"
})
@EnableElasticsearchRepositories(basePackages = "com.jupiter.wyl.domain.movie.movie.repository.elastic")
public class WylApplication {

	public static void main(String[] args) {
		SpringApplication.run(WylApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
