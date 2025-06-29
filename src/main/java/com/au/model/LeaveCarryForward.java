package com.au.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Author: Anjan
 * Date: 28-01-2025
 * Description: LeaveCarryForward class
 */

@Entity
@Data
public class LeaveCarryForward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveCarryForwardId;
    private Integer empId;
    private Integer leavePatternId;
    private Integer carryForwardDays;
    private Integer year;
    private Integer leave_id;
    private String remarks;

}
