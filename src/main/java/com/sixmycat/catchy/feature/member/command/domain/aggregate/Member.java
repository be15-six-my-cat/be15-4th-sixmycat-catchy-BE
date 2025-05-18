package com.sixmycat.catchy.feature.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

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
}
