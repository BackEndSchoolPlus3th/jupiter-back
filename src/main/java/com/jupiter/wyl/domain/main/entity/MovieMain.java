package com.jupiter.wyl.domain.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MovieMain {

    @Id
    private Long id;

    private String title;

    @Column(length = 1000)  // 컬럼의 최대 길이 설정
    private String overview;

    private String posterPath;
    private String category;

    public MovieMain() {
    }

    public MovieMain(Long id, String title, String overview, String posterPath, String category) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.category = category;
    }
}
