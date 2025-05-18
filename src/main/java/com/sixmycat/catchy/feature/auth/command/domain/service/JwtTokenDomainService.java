package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;

public interface JwtTokenDomainService {
    String issueAndRedirect(String baseRedirectUrl, Member member);
}
