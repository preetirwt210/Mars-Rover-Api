package com.spring.mars.MarsRoverApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mars.MarsRoverApi.dto.HomeDTO;

public interface PreferencesRepository extends JpaRepository<HomeDTO, Long> {

	 HomeDTO findByUserId(Long userId);
}
