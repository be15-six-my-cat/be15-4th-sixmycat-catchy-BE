package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우를 하는 사람 (팔로워)
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    // 팔로우 당하는 사람 (팔로잉 대상)
    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}

