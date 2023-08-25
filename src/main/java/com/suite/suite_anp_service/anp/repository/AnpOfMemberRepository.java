package com.suite.suite_anp_service.anp.repository;

import com.suite.suite_anp_service.anp.entity.AnpOfMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnpOfMemberRepository extends JpaRepository<AnpOfMember, Long>  {
}
