package hello.order.v0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV0 {

    @Bean
    public OrderServiceV0 orderService() {
        return new OrderServiceV0();
    }
}
