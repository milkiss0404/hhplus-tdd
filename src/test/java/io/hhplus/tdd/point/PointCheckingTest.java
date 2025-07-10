package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PointCheckingTest {
    @Mock
    UserPointTable userPointTable;
    @Mock
    PointHistoryTable pointHistoryTable;

    @InjectMocks
    PointChecking pointChecking;

    private final Long MAX_POINT = 1_000_000L;

    @Test
    @DisplayName("포인트 잔액 조회")
    void checkingPoint() throws Exception {
        // given
        long currentTime = System.currentTimeMillis();

        UserPoint mockUser = new UserPoint(1L, 1000L, currentTime);
        Mockito.when(userPointTable.insertOrUpdate(1L, 1000L)).thenReturn(mockUser);

        UserPoint userPoint = userPointTable.insertOrUpdate(1L, 1000L);
        Mockito.verify(userPointTable).insertOrUpdate(1L, 1000L);


        PointHistory mockHistory = new PointHistory(1L, userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime);
        Mockito.when(pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime))
                .thenReturn(mockHistory);

        PointHistory mockHistory2 = new PointHistory(1L, userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime);
        Mockito.when(pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime))
                .thenReturn(mockHistory2);

        PointHistory insert1 = pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime);
        PointHistory insert2 = pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime);
        Mockito.verify(pointHistoryTable, Mockito.times(2)).insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, currentTime);

        List<PointHistory> expectedHistory = List.of(insert1, insert2);
        Mockito.when(pointHistoryTable.selectAllByUserId(userPoint.id())).thenReturn(expectedHistory);

        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(userPoint.id());
        Mockito.verify(pointHistoryTable).selectAllByUserId(userPoint.id());

        Mockito.when(userPointTable.insertOrUpdate(userPoint.id(), 2000L))
                .thenReturn(new UserPoint(userPoint.id(), 2000L, currentTime));

        //when
        UserPoint getBalance = pointChecking.getBalance(userPoint.id());

        //then
        Assertions.assertThat(getBalance.point()).isEqualTo(2000L);
        Assertions.assertThat(userPoint.id()).isEqualTo(1L);
    }
}