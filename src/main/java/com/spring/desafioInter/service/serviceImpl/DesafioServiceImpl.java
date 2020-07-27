package com.spring.desafioInter.service.serviceImpl;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.repository.CalculoRepository;
import com.spring.desafioInter.repository.UsuarioRepository;
import com.spring.desafioInter.service.DesafioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesafioServiceImpl implements DesafioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CalculoRepository calculoRepository;

    @Override
    public List<Usuario> finAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findUserById(Long id) {
        return usuarioRepository.findById(id).get();
    }

    @Override
    public Usuario saveUser(Usuario user){
        return usuarioRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Calculo saveCalculo(Calculo calculo){
        return calculoRepository.save(calculo);
    }
}
