package com.sixmycat.catchy.feature.member.query.mapper;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.User;
import com.sixmycat.catchy.feature.member.query.dto.response.*;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    public MyProfileResponse toMyProfileResponse(User user,
                                                 int followerCount,
                                                 int followingCount,
                                                 int postCount,
                                                 Badges badges,
                                                 List<FeedSummary> myFeeds,
                                                 List<VideoSummary> myVideos,
                                                 List<FeedSummary> likedFeeds,
                                                 List<VideoSummary> likedVideos) {

        List<CatResponse> cats = user.getCats().stream()
                .map(this::toCatResponse)
                .collect(Collectors.toList());

        return new MyProfileResponse(
                user.getNickname(),
                user.getStatusMessage(),
                user.getProfileImg(),
                badges,
                followerCount,
                followingCount,
                postCount,
                cats,
                myFeeds,
                myVideos,
                likedFeeds,
                likedVideos
        );
    }

    public CatResponse toCatResponse(Cat cat) {
        return new CatResponse(
                cat.getId(),
                cat.getName(),
                cat.getGender(),
                cat.getBreed(),
                cat.getBirthDay(),
                cat.getAge()
        );
    }
}
