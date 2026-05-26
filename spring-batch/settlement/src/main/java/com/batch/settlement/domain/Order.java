package com.batch.settlement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Table(name = "orders")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName; // 주문자 이름
    private String storeName; // 가맹점 이름
    private Integer amount; // 주문 금액
    private LocalDate orderDate; // 주문 일자 (YYYY-MM-DD)
}
