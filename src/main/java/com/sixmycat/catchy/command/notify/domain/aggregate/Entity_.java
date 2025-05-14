package com.sixmycat.catchy.command.notify.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Entity_ { // 이름을 변경하여 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
