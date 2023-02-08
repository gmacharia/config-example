/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kobe
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "configs")

public class KyConfig implements Serializable {

    @Id

    @JsonProperty("name")
    @Column(name = "config_name")
    private String configName;

    @Embedded
    private Metadata metadata;

}
