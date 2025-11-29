package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 * 자동 커밋 모드를 수동 커밋 모드로 변경하는 것을 '트랜잭션을 시작한다'고 표현한다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(fromId, toId, money, connection); // 비즈니스 로직
            connection.commit(); // 성공시 커밋
        } catch (Exception e) {
            connection.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            release(connection);
        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection connection) throws SQLException {
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(connection, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true); // 커넥션 풀 고려
                connection.close(); // 트랜잭션 종료
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }
}
