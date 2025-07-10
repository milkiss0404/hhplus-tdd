package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PointChecking {
    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;
    private final Long MAX_POINT = 1_000_000L;


    public UserPoint getBalance(Long userId) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(userId);
        long sum = pointHistories.stream().mapToLong(PointHistory::amount).sum();
        return userPointTable.insertOrUpdate(userId, sum);
    }
}