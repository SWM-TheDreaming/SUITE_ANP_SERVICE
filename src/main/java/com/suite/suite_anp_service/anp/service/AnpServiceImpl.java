package com.suite.suite_anp_service.anp.service;

import com.suite.suite_anp_service.anp.dto.ResAnpOfMemberDto;
import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import com.suite.suite_anp_service.anp.repository.AnpOfMemberRepository;
import com.suite.suite_anp_service.exception.RepositoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnpServiceImpl implements AnpService{
    private final AnpOfMemberRepository anpOfMemberRepository;

    public ResAnpOfMemberDto getPoints(Long memberId) {
        AnpOfMember anpOfMember = anpOfMemberRepository.findByMemberId(memberId).orElseThrow(() -> new RepositoryException());
        return anpOfMember.toResAnpOfMemberDto();
    }
}
