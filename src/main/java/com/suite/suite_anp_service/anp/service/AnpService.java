package com.suite.suite_anp_service.anp.service;

import com.suite.suite_anp_service.anp.dto.ResAnpOfMemberDto;

public interface AnpService {
    ResAnpOfMemberDto getPoints(Long memberId);
}
