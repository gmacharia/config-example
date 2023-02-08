/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config.repository;

import com.example.config.entity.KyConfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author kobe
 */
@Repository
public interface KyExRepo extends JpaRepository<KyConfig, Long> {

    List<KyConfig> findByConfigName(String name);

    List<KyConfig> deleteByConfigName(String name);

    @Query(value = "SELECT * FROM configs t where cpu_enabled = ?1",
            nativeQuery = true)
    List<KyConfig> fetchEnabledLimit(String value);

    @Query(value = "SELECT * FROM configs t where monitoring_enabled = ?1",
            nativeQuery = true)
    List<KyConfig> fetchEnabledMonitor(String monitorValue);
}
