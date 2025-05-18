package com.sixmycat.catchy.feature.block.query.service;

import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.mapper.BlockQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BlockQueryServiceImplTest {

    @Mock
    private BlockQueryRepository blockQueryRepository;

    @InjectMocks
    private BlockQueryServiceImpl blockQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBlockedUsers_shouldReturnListOfBlockedUsers() {
        // given
        Long blockerId = 1L;

        List<BlockResponse> mockBlockedList = List.of(
                BlockResponse.builder()
                        .blockedId(2L)
                        .blockedNickname("UserTwo")
                        .blockedAt(LocalDateTime.now().minusDays(1))
                        .build(),
                BlockResponse.builder()
                        .blockedId(3L)
                        .blockedNickname("UserThree")
                        .blockedAt(LocalDateTime.now().minusDays(2))
                        .build()
        );

        when(blockQueryRepository.findBlockedUsers(blockerId)).thenReturn(mockBlockedList);

        // when
        List<BlockResponse> result = blockQueryService.getBlockedUsers(blockerId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getBlockedId()).isEqualTo(2L);
        assertThat(result.get(1).getBlockedNickname()).isEqualTo("UserThree");

        verify(blockQueryRepository, times(1)).findBlockedUsers(blockerId);
    }
}
