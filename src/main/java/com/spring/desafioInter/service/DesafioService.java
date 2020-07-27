package com.spring.desafioInter.service;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;

import java.util.List;

public interface DesafioService {

    List<Usuario> finAllUsers();

    Usuario findUserById(Long id);

    Usuario saveUser(Usuario user);

    void deleteUser(Long id);

    public Calculo saveCalculo(Calculo calculo);

}
