package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    @Query("select m from Member m inner join m.projects project where project.id = ?1")
    List<Member> findByProjectId(Long projectId);

    @Query("select m from Member m where m.username = ?1")
    Member findByUserName(String username);
}
