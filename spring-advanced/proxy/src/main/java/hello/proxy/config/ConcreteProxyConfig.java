package hello.proxy.config;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace trace) {
        OrderControllerV2 target = new OrderControllerV2(orderServiceV2(trace));
        return new OrderControllerConcreteProxy(target, trace);
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace trace) {
        OrderServiceV2 target = new OrderServiceV2(orderRepositoryV2(trace));
        return new OrderServiceConcreteProxy(target, trace);
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace trace) {
        OrderRepositoryV2 target = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(target, trace);
    }
}
