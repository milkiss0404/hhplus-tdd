package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointHistoryTable pointHistoryTable;
    private final PointChecking pointChecking;
    private final UserPointTable userPointTable;

    public UserPoint chargeUserPoint(Long id, Long amount) throws Exception {
        pointChecking.checkBalance(id, amount, TransactionType.CHARGE);

        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return sumAndInsertPoint(id);
    }

    public UserPoint usingUserPoint(Long id,Long amount) throws Exception {
        pointChecking.checkBalance(id,amount,TransactionType.USE);

        pointHistoryTable.insert(id,amount,TransactionType.USE,System.currentTimeMillis());

        return sumAndInsertPoint(id);
    }


    private UserPoint sumAndInsertPoint(Long id) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(id);
        long sum = pointHistories.stream().mapToLong(PointHistory::amount).sum();
        return userPointTable.insertOrUpdate(id, sum);
    }
}