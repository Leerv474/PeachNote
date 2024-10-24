package com.leerv474.peach_note.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("boards")
public class BoardController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
