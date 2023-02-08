/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.model;

import com.example.config.entity.Cpu;
import com.example.config.entity.Metadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kobe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationResponseDTO {

    private String name;
    private Metadata metadata;
    private Cpu cpu;
}
