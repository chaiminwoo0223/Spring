package study.spring.basic.order;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Order {
    private Long id;
    private String itemName;
    private int itemPrice;
    private int discountPrice;

    public int calculatePrice() {
        return itemPrice - discountPrice;
    }
}
