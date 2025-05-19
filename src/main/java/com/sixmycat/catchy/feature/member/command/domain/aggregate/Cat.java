package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDay;

    private Integer age;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자 (등록 시 필요한 필드들만 받는 생성자)
    public Cat(String name, String gender, String breed, LocalDate birthDay, Integer age, Member member) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.birthDay = birthDay;
        this.age = age;
        this.member = member;
    }

    // 고양이 정보 수정 메서드
    public void updateCatInfo(String name, String gender, String breed, LocalDate birthDay, Integer age) {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.birthDay = birthDay;
        this.age = age;
    }
}
