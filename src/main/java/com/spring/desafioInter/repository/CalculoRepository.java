package com.spring.desafioInter.repository;

import com.spring.desafioInter.model.Calculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculoRepository extends JpaRepository<Calculo, Long> {
}
