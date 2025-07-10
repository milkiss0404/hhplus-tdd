package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PointServiceTest {
    @Mock
    UserPointTable userPointTable;
    @Mock
    PointHistoryTable pointHistoryTable;
    @Mock
    PointChecking pointChecking;

    @InjectMocks
    PointService pointService;

    @Test
    @DisplayName("포인트 충전시 히스토리 합계 계산후 유저 포인트에 입력")
    void charge_point() throws Exception {
        // given
        long currentTime = System.currentTimeMillis();

        UserPoint mockUser = new UserPoint(1L, 1000L, currentTime);
        when(userPointTable.insertOrUpdate(1L, 1000L)).thenReturn(mockUser);


        PointHistory pointHistory = new PointHistory(1L, mockUser.id(), mockUser.point(), TransactionType.CHARGE, currentTime);
        when(pointHistoryTable.insert(eq(mockUser.id()), eq(mockUser.point()), eq(TransactionType.CHARGE), anyLong()))
                .thenReturn(pointHistory);

        List<PointHistory> pointHistoryList = List.of(pointHistory);
        when(pointHistoryTable.selectAllByUserId(mockUser.id())).thenReturn(pointHistoryList);

        // when
        UserPoint chargeUser = pointService.chargeUserPoint(mockUser.id(), mockUser.point());

        // then
        verify(userPointTable).insertOrUpdate(mockUser.id(), mockUser.point());
        verify(pointHistoryTable).insert(eq(mockUser.id()), eq(mockUser.point()), eq(TransactionType.CHARGE), anyLong());
        verify(pointHistoryTable).selectAllByUserId(mockUser.id());

        Assertions.assertThat(chargeUser.point()).isEqualTo(mockUser.point());
    }
}