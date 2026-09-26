package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDTO;
import jpabook.jpashop.repository.order.query.OrderItemQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // V2: 엔티티를 DTO로 변환
    @GetMapping("/v2/orders")
    public List<OrderDTO> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDTO> result  = orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
        return result;
    }

    // V3: 엔티티를 DTO로 변환(fetch join)
    @GetMapping("/v3/orders")
    public List<OrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        for (Order order : orders) {
            System.out.println("order ref = " + order + " id = " + order.getId());
        }
        List<OrderDTO> result = orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
        return result;
    }

    // V4: 엔티티를 DTO로 변환(페이징과 한계 돌파)
    @GetMapping("/v4/orders")
    public List<OrderDTO> ordersV4(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                   @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDTO> result = orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
        return result;
    }

    // V5: JPA에서 DTO 직접 조회
    @GetMapping("/v5/orders")
    public List<OrderQueryDTO> ordersV5() {
        return orderQueryRepository.findOrderQueryDTOs();
    }

    // V6: JPA에서 DTO 직접 조회(컬렉션 조회 최적화)
    @GetMapping("/v6/orders")
    public List<OrderQueryDTO> ordersV6() {
        return orderQueryRepository.findAllByDTO_optimization();
    }

    // V7: JPA에서 DTO 직접 조회(플랫 데이터 최적화)
    @GetMapping("/v7/orders")
    public List<OrderQueryDTO> ordersV7() {
        List<OrderFlatDTO> flats = orderQueryRepository.findAllByDTO_flat();
        return flats.stream()
                .collect(groupingBy(
                        o -> new OrderQueryDTO(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDTO(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                ))
                .entrySet().stream()
                .map(e -> new OrderQueryDTO(
                        e.getKey().getOrderId(),
                        e.getKey().getName(),
                        e.getKey().getOrderDate(),
                        e.getKey().getOrderStatus(),
                        e.getKey().getAddress(),
                        e.getValue()))
                .collect(toList());
    }

    @Data
    static class OrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;

        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDTO {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}