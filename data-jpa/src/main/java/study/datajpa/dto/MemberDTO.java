package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDTO {
    Long id;
    String username;
    String teamName;

    public MemberDTO(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}