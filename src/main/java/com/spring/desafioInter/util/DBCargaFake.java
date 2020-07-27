package com.spring.desafioInter.util;

import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBCargaFake {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostConstruct
    public void createDummyPosts(){

        List<Usuario> usuarios = new ArrayList<>();

        Usuario u1 = new Usuario();
        u1.setNome("Johnny Contiero");
        u1.setEmail("j.contiero@hotmail.com");
        usuarios.add(u1);

        Usuario u2 = new Usuario();
        u2.setNome("Fake Um");
        u2.setEmail("fake1@test.com.br");
        usuarios.add(u2);

        Usuario u3 = new Usuario();
        u3.setNome("Fake Dois");
        u3.setEmail("fake2@test.com.br");
        usuarios.add(u3);

        usuarioRepository.saveAll(usuarios);
    }
}
