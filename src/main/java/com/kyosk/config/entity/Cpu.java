/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kobe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Cpu {

    @JsonProperty("enabled")	
    @Column(insertable = false, updatable = false)
    private String cpu_enabled;
    
    @JsonProperty("value")
    @Column(insertable = false, updatable = false)
    private String cpu_value;
}
