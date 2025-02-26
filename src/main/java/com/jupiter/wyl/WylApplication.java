package com.jupiter.wyl;

import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;
@EnableJpaAuditing
@SpringBootApplication
public class WylApplication {

	public static void main(String[] args) {
		SpringApplication.run(WylApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
