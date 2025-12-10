package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TxBasicTest {
    @Autowired BasicService service;

    @Test
    void proxyCheck() {
        log.info("aop class={}", service.getClass());
        assertThat(AopUtils.isAopProxy(service)).isTrue();
    }

    @Test
    void txTest() {
        service.tx();
        service.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {

        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void tx() {
            log.info("call tx");
            printTxInfo();
        }

        public void nonTx() {
            log.info("call nonTx");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}", txActive);
        }
    }
}
