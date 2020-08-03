package com.spring.desafioInter.service.serviceImpl;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.repository.CalculoRepository;
import com.spring.desafioInter.repository.UsuarioRepository;
import com.spring.desafioInter.service.DesafioService;
import com.spring.desafioInter.util.CacheCalculos;
import com.spring.desafioInter.util.CalcularDigitoUnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesafioServiceImpl implements DesafioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    CacheCalculos cache = new CacheCalculos();

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

    /**
     * Processa o calculo do digito unico
     * @param calculo
     * @param usuarioID
     * @return
     */
    @Override
    public Calculo processarCalculo(Calculo calculo, Long usuarioID){

        Calculo calculoCache = cache.getCalculo(calculo);

        if (calculoCache == null) {

            CalcularDigitoUnico calcularDigito = new CalcularDigitoUnico();
            Long digitoUnico = calcularDigito.calcular(calculo.getNumero(), calculo.getQuantidadeRepeticoes());
            calculo.setDigitoUnico(digitoUnico);
            cache.enfileirar(calculo);
        }
        else
        {
            calculo = calculoCache;
        }

        Usuario usuario = findUserById(usuarioID);
        calculo.setUsuario(usuario);

        saveCalculo(calculo);

        return  calculo;
    }

    /**
     * Valida se usuario esta populado
     * @param usuario
     * @param isEditar
     * @return
     */
    public boolean isUsuarioOK(Usuario usuario, boolean isEditar){

        if (usuario != null
                && usuario.getNome() != null
                && usuario.getEmail() != null) {

            if (isEditar && usuario.getId() == null){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Persiste usuario da chamda rest
     * @param usuario
     * @param isEditar
     * @return
     */
    public ResponseEntity<Object> saveUserAPI(Usuario usuario, boolean isEditar){
        if (isUsuarioOK(usuario, isEditar)) {
            saveUser(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Request Invalido",HttpStatus.BAD_REQUEST);
        }
    }
}
