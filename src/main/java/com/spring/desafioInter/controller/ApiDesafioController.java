package com.spring.desafioInter.controller;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.service.DesafioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Api(value="API Rest")
@Controller
@RequestMapping(value="/desafio")
public class ApiDesafioController {

    @Autowired
    DesafioService desafioService;


    /**
     * Escluir usuário
     * @param id Identificação do usuário
     * @return redireciona pagina de usuarios
     */
    @ApiOperation(value="Excluir cadastro do usuário")
    @GetMapping(path = {"/excluirUsuario/{id}"})
    public ResponseEntity<Object> getExcluirUsuario(@PathVariable("id") Long id){
        desafioService.deleteUser(id);
        return new ResponseEntity<>("Exclusão realizada com sucesso!", HttpStatus.OK);
    }

    /**
     *
     * @param usuario
     * @return
     */
    @ApiOperation(value="Inserir cadastro do usuário")
    @PostMapping(value="/newUsuario")
    public ResponseEntity<Object> salvarUsuarioAPI(@Valid @RequestBody Usuario usuario){
        return desafioService.saveUserAPI(usuario, false);
    }

    /**
     *
     * @param usuario
     * @return
     */
    @ApiOperation(value="Editar cadastro do usuário")
    @PostMapping(value="/editarUsuario")
    public ResponseEntity<Object> editarUsuarioAPI(@Valid @RequestBody Usuario usuario){
        return desafioService.saveUserAPI(usuario, true);
    }

    /**
     *
     * @param calculo
     * @param usuarioID
     * @return
     */
    @ApiOperation(value="Calcular dígito unico")
    @PostMapping(value="/calcularDigitoUnico/{id}")
    public ResponseEntity<Object> calcularUnicoDigito(@Valid @RequestBody Calculo calculo, @PathVariable("id") Long usuarioID){

        calculo = desafioService.processarCalculo(calculo,usuarioID);
        return new ResponseEntity<>("O digito único calculado foi: " + calculo.getDigitoUnico(), HttpStatus.OK);
    }

    /**
     *
     * @param usuarioID
     * @return
     */
    @ApiOperation(value="Retorna lista de cálculos do usuário")
    @GetMapping(value="/getCalulosByUsuario/{id}")
    public ResponseEntity<List<Calculo>> getCalulosByUsuario(@PathVariable("id") Long usuarioID){

        Usuario usuario = desafioService.findUserById(usuarioID);

        return new ResponseEntity<>(usuario.getCalculos(), HttpStatus.OK);
    }


}

