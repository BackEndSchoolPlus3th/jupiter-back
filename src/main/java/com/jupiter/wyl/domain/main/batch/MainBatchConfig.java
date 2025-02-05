package com.jupiter.wyl.domain.main.batch;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class MainBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Lazy
    private final MovieMainService movieMainService;

    @Autowired
    public MainBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MovieMainService movieMainService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.movieMainService = movieMainService;
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory() {
        return jobBuilderFactory;
    }

    // Job 정의: Step을 순차적으로 실행
    @Bean
    public Job movieBatchJob() {
        return jobBuilderFactory.get("movieBatchJob")
                .start(movieStep())
                .build();
    }

    // Step 정의 (Reader -> Processor -> Writer)
    @Bean
    public Step movieStep() {
        return stepBuilderFactory.get("movieStep")
                .<MovieMainDto, MovieMainDto>chunk(10)
                .reader(movieItemReader())
                .processor(movieItemProcessor())
                .writer(movieItemWriter())
                .build();
    }

    // Step 1: 영화 데이터를 API에서 읽기 (ItemReader)
    @Bean
    @Lazy
    public MainItemReader movieItemReader() {
        return new MainItemReader(movieMainService);  // ItemReader 정의
    }

    // Step 2: 영화 데이터 처리 (ItemProcessor)
    @Bean
    public MainItemProcessor movieItemProcessor() {
        return new MainItemProcessor();  // ItemProcessor 구현
    }

    // Step 3: 영화 데이터를 DB에 저장하기 (ItemWriter)
    @Bean
    @Lazy
    public MainItemWriter movieItemWriter() {
        return new MainItemWriter(movieMainService); // MovieItemWriter 생성
    }
}
