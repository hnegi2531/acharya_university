package com.au.controller;

import com.au.model.Triggers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Anjan
 * Date: 18-12-2024
 * Description: TriggerRepository class
 */

@Repository
public interface TriggerRepository extends JpaRepository<Triggers, Integer> {
    Optional<Triggers> findByName(String triggerName);
}
