package com.au.repository;

import com.au.model.SchedulerLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerLockRepository extends JpaRepository<SchedulerLock, Long> {

    Optional<SchedulerLock> findByJobName(String jobName);
}
