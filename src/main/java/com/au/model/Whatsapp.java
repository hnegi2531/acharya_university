package com.au.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Author: Anjan
 * Date: 14-12-2024
 * Description: Whatsapp class
 */

@Data
@Entity
public class Whatsapp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer whatsappId;
    private String status;
    private String otp;
    private String phone;
    private Integer userId;
    private String userName;
    private LocalDateTime time;
}
