/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.controller;

import com.kyosk.config.entity.KyoskConfig;
import com.kyosk.config.service.ConfigurationService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kobe
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("kyosk/api")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    /**
     * Here we are just fetching all configs mapped in the Db
     *
     * @return - Return all Configs.
     */
    @GetMapping("/configs")
    public ResponseEntity<Object> onFetchAllConfigs() {
        log.info("Fetching all configurations");
        return new ResponseEntity<>(configurationService.fetchKyoskConfigs(), HttpStatus.OK);
    }

    /**
     * Here we are just fetching configs mapped in the Db by name
     *
     * @param name
     * @return
     */
    @GetMapping("/configs/{name}")
    public ResponseEntity<Object> onFetchConfigsByName(@PathVariable("name") String name) {
        log.info("Fetching all configurations");
        return new ResponseEntity<>(configurationService.fetchKyoskConfigsByName(name), HttpStatus.OK);
    }

    /**
     * Here we are just fetching specific configs mapped in the Db
     *
     * @param param
     * @return - Return specific Configs.
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchMapByParam(@RequestParam Map<String, String> param) {
        log.info("Validating which key to fetch [{}]", param.entrySet());
        List<?> response = configurationService.searchMapByParam(param);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Here we are just creating a new config in the Db
     *
     * @param kyoskConfig
     * @return
     */
    @PostMapping(value = "/configs/create")
    public ResponseEntity<Object> createConfigDeta(@Valid @RequestBody KyoskConfig kyoskConfig) {
        return new ResponseEntity<>(configurationService.createConfigRecord(kyoskConfig), HttpStatus.OK);
    }

    /**
     * Here we are deleting a config in the Db
     *
     * @param name
     * @return 
     */
    @DeleteMapping("/configs/{name}")
    public ResponseEntity<Object> deleteConfigData(@PathVariable("name") String name) {
        log.info("Fetching all configs with the name [{}]", name);
        return new ResponseEntity<>(configurationService.deleteConfigData(name), HttpStatus.OK);
    }
}
