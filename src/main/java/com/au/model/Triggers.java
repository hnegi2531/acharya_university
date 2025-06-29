package com.au.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Author: Anjan
 * Date: 18-12-2024
 * Description: Triggers class
 */

@Data
@Entity
public class Triggers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String status;
}
