package com.au.repository;

import com.au.model.Whatsapp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Anjan
 * Date: 14-12-2024
 * Description: WhatsappRepository class
 */

public interface WhatsappRepository extends JpaRepository<Whatsapp, Integer> {
    Whatsapp findByPhone(String phone);
}
