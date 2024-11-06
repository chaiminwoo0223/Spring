package study.spring.introduction.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // JPA가 관리하는 엔티티
@Getter
@Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
