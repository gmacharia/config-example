/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.entity;

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
public class Metadata {

    private Monitoring monitoring;
    private Limits limits;
}
