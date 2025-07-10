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

    public void checkBalance(Long id, Long point, TransactionType type) throws Exception {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(id);
        long sum = pointHistories.stream().mapToLong(PointHistory::amount).sum();
        if (type == TransactionType.CHARGE) {
            if (point > MAX_POINT) {
                throw new Exception("충전 금액은 1,000,000 을 넘을수 없습니다");
            }
        } else if (type == TransactionType.USE) {
            if (sum < point) {
                throw new Exception("잔액보다 사용금액이 더많습니다");
            }
        }
    }
}