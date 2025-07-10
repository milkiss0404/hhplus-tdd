package io.hhplus.tdd.database;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PointHistoryTableTest {
    @Mock
    UserPointTable userPointTable;

    @InjectMocks
    PointHistoryTable pointHistoryTable;

    @Test
    @DisplayName("유저 포인트 히스토리 생성")
    void insertPointTable() throws Exception {
        //given
        UserPoint mockUserPoint = new UserPoint(1L,1000L,System.currentTimeMillis());
        Mockito.when(userPointTable.insertOrUpdate(1L, 1000L))
                .thenReturn(mockUserPoint);
        //when
        UserPoint userPoint = userPointTable.insertOrUpdate(1L, 1000L);
        Mockito.verify(userPointTable).insertOrUpdate(1L, 1000L);
        PointHistory insert = pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, System.currentTimeMillis());
        //then
        Assertions.assertThat(insert).isNotNull();
    }
    @Test
    @DisplayName("유저 아이디별 히스토리 조회")
    void selectAllByUserId () throws Exception {
        //given
        UserPoint mockUserPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
        Mockito.when(userPointTable.insertOrUpdate(1L, 1000L))
                .thenReturn(mockUserPoint);
        //when
        UserPoint userPoint = userPointTable.insertOrUpdate(1L, 1000L);
        Mockito.verify(userPointTable).insertOrUpdate(1L, 1000L);

        PointHistory insert = pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, System.currentTimeMillis());
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(insert.userId());
        //then
        Assertions.assertThat(pointHistories.get(0).userId()).isEqualTo(insert.userId());
    }
}
