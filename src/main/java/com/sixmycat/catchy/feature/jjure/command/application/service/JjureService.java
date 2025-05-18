package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;

public interface JjureService {
    void uploadJjure(JjureUploadRequest request, Long memberId);
}

