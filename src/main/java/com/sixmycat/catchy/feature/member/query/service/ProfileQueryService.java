package com.sixmycat.catchy.feature.member.query.service;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.User;
import com.sixmycat.catchy.feature.member.command.domain.repository.CatRepository;
import com.sixmycat.catchy.feature.member.command.domain.repository.FollowRepository;
import com.sixmycat.catchy.feature.member.command.domain.repository.UserRepository;
import com.sixmycat.catchy.feature.member.query.dto.response.*;
import com.sixmycat.catchy.feature.member.query.mapper.ProfileMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileQueryService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    // 다른 도메인에서 주입될 Repository (post, video 등)
    // private final PostRepository postRepository;
    // private final VideoRepository videoRepository;

    public MyProfileResponse getMyProfile(Long userId) {
        // 1. 유저 + 고양이 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 2. 고양이 정보로 생일 뱃지 판단
        boolean isBirthday = user.getCats().stream()
                .anyMatch(cat -> {
                    LocalDate birthDay = cat.getBirthDay();
                    return birthDay != null
                            && birthDay.getMonthValue() == LocalDate.now().getMonthValue()
                            && birthDay.getDayOfMonth() == LocalDate.now().getDayOfMonth();
                });

        // 3. 인플루언서 여부 (팔로워 5만 이상)
        int followerCount = followRepository.countByFollower_Id(userId);
        boolean isInfluencer = followerCount >= 50000;

        // 4. 팔로잉 수
        int followingCount = followRepository.countByFollowing_Id(userId);

        // 5. 게시글 수
        int postCount = 0; // postRepository.countByUserId(userId);

        // 6. 랭킹은 임시 처리 (실제로는 별도 서비스에서 계산해야 정확)
        int rank = 0; // 필요 시 랭킹 계산 로직 추가

        Badges badges = new Badges(rank == 1, isInfluencer, isBirthday);

        // 7. 탭 콘텐츠 목록 (지금은 빈 값, 실제 구현 시 Repository에서 불러옴)
        List<FeedSummary> myFeeds = List.of();        // postRepository.findTop4ByUserId...
        List<VideoSummary> myVideos = List.of();      // videoRepository.findTop4ByUserId...
        List<FeedSummary> likedFeeds = List.of();     // postRepository.findTop4ByLikedUsers...
        List<VideoSummary> likedVideos = List.of();   // videoRepository.findTop4ByLikedUsers...

        // 8. Mapper로 변환해서 반환
        return profileMapper.toMyProfileResponse(
                user,
                followerCount,
                followingCount,
                postCount,
                badges,
                myFeeds,
                myVideos,
                likedFeeds,
                likedVideos
        );
    }
}
