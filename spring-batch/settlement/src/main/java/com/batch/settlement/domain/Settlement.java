package com.batch.settlement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // 주문 번호
    private String storeName; // 가맹점 이름
    private Integer amount; // 정산 금액
    private LocalDate settlementDate; // 정산 일자 (YYYY-MM-DD)

    public Settlement(Long orderId, String storeName, Integer amount, LocalDate settlementDate) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.amount = amount;
        this.settlementDate = settlementDate;
    }
}
