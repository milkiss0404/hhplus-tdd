package io.hhplus.tdd.database;

import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) //junit 테스트 클래스에서 스프링기능을 쓸수있게
@ContextConfiguration(classes = UserPointTable.class)
class UserPointTableTest {

    @Autowired
    UserPointTable userPointTable;

    @Test
    @DisplayName("아이디로 유저찾기")
    void selectById() throws Exception {
        //given
        UserPoint userPoint = userPointTable.insertOrUpdate(1L, 1000L);
        //when
        UserPoint getUserPoint = userPointTable.selectById(userPoint.id());
        //then
        assertThat(getUserPoint).isEqualTo(userPoint);
    }


    @Test
    @DisplayName("유저 생성")
    void insertOrUpdate () throws Exception {
        //given
        UserPoint userPoint = userPointTable.insertOrUpdate(1L, 1000L);
        //when
        //then
        assertThat(userPoint).isNotNull();
    }

}
