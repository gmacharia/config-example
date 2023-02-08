/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.service;

import com.example.config.entity.KyConfig;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

/**
 * @author kobe
 */
public interface ServiceConfigImpl {

    List<KyConfig> fetchKyoskConfigs();

    List<?> searchMapByParam(Map mapValue);

    ResponseEntity<Object> fetchKyoskConfigsByName(String name);

    ResponseEntity<Object> createConfigRecord(KyConfig configurationRequestDT);

    ResponseEntity<Object> deleteConfigData(String name);

    ResponseEntity<Object> updateConfigRecord(KyConfig payload);


}
