/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kobe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Monitoring {

    @JsonProperty("enabled")
    private String monitoring_enabled;
}
