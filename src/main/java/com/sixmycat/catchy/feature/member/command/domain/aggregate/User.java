package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    @Column(name = "status_message")
    private String statusMessage;

    @Column(name = "profile_image")
    private String profileImg;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> cats = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();


    // 생성자
    public User(String nickname, String statusMessage, String profileImg) {
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImg = profileImg;
    }

    // 고양이 추가 헬퍼 메서드 (양방향 설정)
    public void addCat(Cat cat) {
        cats.add(cat);
        cat.setUser(this);
    }
}
