package hello.order.v1;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV1 {

    @Bean
    public OrderServiceV1 orderService(MeterRegistry registry) {
        return new OrderServiceV1(registry);
    }
}
