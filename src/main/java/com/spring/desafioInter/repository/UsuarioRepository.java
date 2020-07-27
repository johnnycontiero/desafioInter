package com.spring.desafioInter.repository;

import com.spring.desafioInter.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
