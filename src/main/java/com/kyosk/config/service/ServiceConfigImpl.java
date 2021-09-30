/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.service;

import com.kyosk.config.entity.KyoskConfig;
import com.kyosk.config.model.ConfigurationResponseDTO;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author kobe
 */
public interface ServiceConfigImpl {

    List<KyoskConfig> fetchKyoskConfigs();

    List<?> searchMapByParam(Map mapValue);

     ResponseEntity<Object> fetchKyoskConfigsByName(String name);
    
    ResponseEntity<Object> createConfigRecord(KyoskConfig configurationRequestDT);
    
    ResponseEntity<Object> deleteConfigData(String name);
    
    ResponseEntity<Object> updateConfigRecord(KyoskConfig payload);
    
    
}
