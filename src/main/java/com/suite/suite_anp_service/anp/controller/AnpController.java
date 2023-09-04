package com.suite.suite_anp_service.anp.controller;

import com.suite.suite_anp_service.anp.dto.ResAnpOfMemberDto;
import com.suite.suite_anp_service.anp.service.AnpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnpController {

    private final AnpService anpService;

    @GetMapping("/anp/point/{memberId}")
    public ResponseEntity<ResAnpOfMemberDto> getPointsByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(anpService.getPoints(memberId));

    }
}
