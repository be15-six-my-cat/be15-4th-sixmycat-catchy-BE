package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String social;

    private String contactNumber;

    private String profileImage;

    private String nickname;

    private String statusMessage;

    @Column(insertable = false)
    private Date createdAt;

    @Column(insertable = false)
    private Date updatedAt;

    private Date deletedAt;

    // 고양이 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cat> cats = new ArrayList<>();

    // 나를 팔로우하는 사람들 (followers)
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    // 내가 팔로우한 사람들 (followings)
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    // 생성자
    public Member(String nickname, String statusMessage, String profileImage) {
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImage = profileImage;
    }

    // 고양이 추가
    public void addCat(Cat cat) {
        cats.add(cat);
        cat.setMember(this);
    }
}
