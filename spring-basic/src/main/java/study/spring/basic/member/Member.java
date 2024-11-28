package study.spring.basic.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Member {
    private Long id;
    private String name;
    private Grade grade;
}
