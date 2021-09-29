/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyosk.config.entity.Cpu;
import com.kyosk.config.entity.KyoskConfig;
import com.kyosk.config.entity.Limits;
import com.kyosk.config.entity.Metadata;
import com.kyosk.config.entity.Monitoring;
import com.kyosk.config.repository.KyoskRepo;
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

/**
 *
 * @author kobe
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConfigurationService implements ServiceConfigImpl {

    private final KyoskRepo kyoskRepository;
    private final ObjectMapper objectMapper;

    @Value("${apps.conig.kyosk.limit}")
    String searchLimitKey;

    @Value("${apps.conig.kyosk.monitoring}")
    String searchMonitoringKey;

    @Override
    public List<KyoskConfig> fetchKyoskConfigs() {
        return kyoskRepository.findAll();
    }

    @Override
    public List<KyoskConfig> fetchKyoskConfigsByName(String name) {
        return kyoskRepository.findByConfigName(name);
    }

    @Override
    public ResponseEntity<Object> deleteConfigData(String configName) {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<KyoskConfig> configExists = kyoskRepository.findByConfigName(configName);

        if (configExists.isEmpty()) {
            responseMap.put("Message", configName + " Not found");
            return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }
        // let's delete the config.
        kyoskRepository.deleteByConfigName(configName);
        responseMap.put("Message", "Deletion was successful");
        responseMap.put("Transactions", kyoskRepository.findAll());

        return new ResponseEntity<>(responseMap, HttpStatus.ACCEPTED);
    }

    @Override
    public List<?> searchMapByParam(Map mapValue) {

        //let's check string passed and what we have on props file
        List<KyoskConfig> response = null;
        try {

            if (mapValue.containsKey(searchLimitKey)) {
                //lets get the value
                String limitValue = mapValue.get(searchLimitKey).toString();

                log.info("Value passed to Db [{}]", limitValue);

                List<KyoskConfig> enabledLimit = kyoskRepository.fetchEnabledLimit(limitValue);
                response = enabledLimit;

            } else if (mapValue.containsKey(searchMonitoringKey)) {
                String monitorValue = mapValue.get(searchMonitoringKey).toString();

                List<KyoskConfig> enabledMonitored = kyoskRepository.fetchEnabledMonitor(monitorValue);
                response = enabledMonitored;

            } else {

            }
            log.info("Response back to user [{}]", objectMapper.writeValueAsString(response.toString()));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConfigurationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> createConfigRecord(KyoskConfig configurationRequestDT) {
        log.info("sanitized payload received {}", configurationRequestDT.toString());

        Map<String, Object> response = new HashMap<>();

        KyoskConfig entity = new KyoskConfig();
        Metadata metadata = new Metadata();
        Limits limits = new Limits();
        Monitoring monitoring = new Monitoring();

        try {

            monitoring.setMonitoring_enabled(configurationRequestDT.getMetadata().getMonitoring().getMonitoring_enabled());

            Cpu cpu = new Cpu();
            cpu.setCpu_enabled(configurationRequestDT.getMetadata().getLimits().getCpu().getCpu_enabled());
            cpu.setCpu_value(configurationRequestDT.getMetadata().getLimits().getCpu().getCpu_value());

            limits.setCpu(cpu);

            metadata.setMonitoring(monitoring);
            metadata.setLimits(limits);

            entity.setConfigName(configurationRequestDT.getConfigName());
            entity.setMetadata(metadata);

            log.info("before saving response {}", objectMapper.writeValueAsString(entity));
            kyoskRepository.save(entity);
        } catch (JsonProcessingException e) {
            //return any db excecption caught
            log.info("Error caught {}", e.getMessage());
        }
        //lets return the status to the user
        return new ResponseEntity<>(entity, HttpStatus.ACCEPTED);
    }

}
