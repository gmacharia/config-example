/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kyosk.config.repository;

import com.kyosk.config.entity.KyoskConfig;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kobe
 */
@Repository
public interface KyoskRepo extends JpaRepository<KyoskConfig, Long> {

    List<KyoskConfig> findByConfigName(String name);
    
    List<KyoskConfig> deleteByConfigName(String name);

    @Query(value = "SELECT * FROM configs t where cpu_enabled = ?1",
            nativeQuery = true)
    List<KyoskConfig> fetchEnabledLimit(String value);

    @Query(value = "SELECT * FROM configs t where monitoring_enabled = ?1",
            nativeQuery = true)
    List<KyoskConfig> fetchEnabledMonitor(String monitorValue);
}
