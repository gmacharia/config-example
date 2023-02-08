/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.config.entity.Cpu;
import com.example.config.entity.KyConfig;
import com.example.config.entity.Limits;
import com.example.config.entity.Metadata;
import com.example.config.entity.Monitoring;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.config.repository.KyExRepo;

/**
 * @author kobe
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SharedFunction {

    private final KyExRepo kyExRepo;
    private final ObjectMapper objectMapper;

    public ResponseEntity<Object> populateConfigRecord(KyConfig updatePayload) {
        log.info("sanitized payload received before ammending {}", updatePayload.toString());

        Map<String, Object> responseMap = new HashMap<>();

        KyConfig entity = new KyConfig();
        Metadata metadata = new Metadata();
        Limits limits = new Limits();
        Monitoring monitoring = new Monitoring();

        try {

            monitoring.setMonitoring_enabled(updatePayload.getMetadata().getMonitoring().getMonitoring_enabled());

            Cpu cpu = new Cpu();
            cpu.setCpu_enabled(updatePayload.getMetadata().getLimits().getCpu().getCpu_enabled());
            cpu.setCpu_value(updatePayload.getMetadata().getLimits().getCpu().getCpu_value());

            limits.setCpu(cpu);

            metadata.setMonitoring(monitoring);
            metadata.setLimits(limits);

            entity.setConfigName(updatePayload.getConfigName());
            entity.setMetadata(metadata);

            log.info("before saving response {}", objectMapper.writeValueAsString(entity));
            kyExRepo.save(entity);
        } catch (JsonProcessingException e) {
            //return any db excecption caught
            log.info("Error caught {}", e.getMessage());
            responseMap.put("ErrorMessage", e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //lets return the status to the user
        return new ResponseEntity<>(entity, HttpStatus.ACCEPTED);
    }
}
