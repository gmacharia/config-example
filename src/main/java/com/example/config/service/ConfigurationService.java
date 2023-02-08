/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.config.entity.KyConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.config.repository.KyExRepo;

/**
 * @author kobe
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConfigurationService implements ServiceConfigImpl {

    private final KyExRepo kyExRepository;
    private final ObjectMapper objectMapper;
    private final SharedFunction sharedFunction;

    @Value("${apps.config.kyosk.limit}")
    String searchLimitKey;

    @Value("${apps.config.kyosk.monitoring}")
    String searchMonitoringKey;

    @Override
    public List<KyConfig> fetchKyoskConfigs() {
        return kyExRepository.findAll();
    }

    @Override
    public ResponseEntity<Object> fetchKyoskConfigsByName(String name) {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<KyConfig> configExists = kyExRepository.findByConfigName(name);

        if (configExists.isEmpty()) {
            responseMap.put("Message", name + " Not found");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        } else {
            responseMap.put("Transactions", kyExRepository.findByConfigName(name));
            return new ResponseEntity<>(responseMap, HttpStatus.ACCEPTED);
        }
    }

    @Override
    public ResponseEntity<Object> deleteConfigData(String configName) {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<KyConfig> configExists = kyExRepository.findByConfigName(configName);

        if (configExists.isEmpty()) {
            responseMap.put("Message", configName + " Not found");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }
        // let's delete the config.
        kyExRepository.deleteByConfigName(configName);
        responseMap.put("Message", "Deletion was successful");
        responseMap.put("Transactions", kyExRepository.findAll());

        return new ResponseEntity<>(responseMap, HttpStatus.ACCEPTED);
    }

    @Override
    public List<?> searchMapByParam(Map mapValue) {

        //let's check string passed and what we have on props file
        List<KyConfig> response = null;

        try {

            if (mapValue.containsKey(searchLimitKey)) {
                //lets get the value
                String limitValue = mapValue.get(searchLimitKey).toString();

                log.info("Value passed to Db [{}]", limitValue);

                List<KyConfig> enabledLimit = kyExRepository.fetchEnabledLimit(limitValue);
                response = enabledLimit;

            } else if (mapValue.containsKey(searchMonitoringKey)) {
                String monitorValue = mapValue.get(searchMonitoringKey).toString();

                List<KyConfig> enabledMonitored = kyExRepository.fetchEnabledMonitor(monitorValue);
                response = enabledMonitored;

            }
            log.info("Response back to user [{}]", objectMapper.writeValueAsString(response.toString()));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConfigurationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> createConfigRecord(KyConfig configurationRequestDT) {
        log.info("sanitized payload received before saving {}", configurationRequestDT.toString());
        //lets return the status to the user
        return sharedFunction.populateConfigRecord(configurationRequestDT);
    }

    @Override
    public ResponseEntity<Object> updateConfigRecord(KyConfig updatePayload) {
        log.info("lets do the update {}", updatePayload.toString());

        return sharedFunction.populateConfigRecord(updatePayload);
    }
}
