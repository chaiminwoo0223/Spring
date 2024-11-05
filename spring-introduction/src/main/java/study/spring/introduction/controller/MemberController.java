package study.spring.introduction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import study.spring.introduction.service.MemberService;

@Controller // Spring Container에 Spring Bean 등록(@Component)
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
