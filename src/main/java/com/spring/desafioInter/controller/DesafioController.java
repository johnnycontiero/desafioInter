package com.spring.desafioInter.controller;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.service.DesafioService;
import com.spring.desafioInter.util.CacheCalculos;
import com.spring.desafioInter.util.CalcularDigitoUnico;
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
public class DesafioController {

    @Autowired
    DesafioService desafioService;

    @GetMapping(path = {"/"})
    public String index(){
        return "redirect:/usuarios";
    }

    /**
     * Obtém lista de usuários
     * @return ModelAndView
     */
    @GetMapping(path = {"/usuarios"})
    public ModelAndView getUsuarios(){
        ModelAndView mv = new ModelAndView( "usuarios");
        List<Usuario> usuarios = desafioService.finAllUsers();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    /**
     * Detalhar cadastro de um usuário
     * @param id Identificação do usuário
     * @return ModelAndView
     */
    @GetMapping(path = {"/usuarios/{id}"})
    public ModelAndView getDetalhesUsuario(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "detalhesUsuario");
        Usuario usuario = desafioService.findUserById(id);
        mv.addObject("usuario", usuario);
        return mv;
    }

    /**
     * Direciona para pagina de cadastro de usuário
     * @return pagina destino
     */
    @GetMapping(path = {"/newUsuario"})
    public String getInserirUsuarioForm(){
        return "inserirUsuario";
    }

    /**
     * Insere novo usuário
     * @param usuario
     * @return redireciona pagina de usuarios
     */
    @PostMapping(value="/newUsuario")
    public String salvarUsuario(@Valid @ModelAttribute Usuario usuario){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Escluir usuário
     * @param id Identificação do usuário
     * @return redireciona pagina de usuarios
     */
    @ApiOperation(value="Excluir cadastro do usuário")
    @GetMapping(path = {"/excluirUsuario/{id}"})
    public String getExcluirUsuario(@PathVariable("id") Long id){
        desafioService.deleteUser(id);
        return "redirect:/usuarios";
    }

    /**
     * Direciona para pagina de editar cadastro de usuário
     * @return pagina destino
     */
    @GetMapping(path = {"/editarUsuario/{id}"})
    public ModelAndView getEditarUsuario(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "editarUsuario");
        Usuario usuario = desafioService.findUserById(id);
        mv.addObject("usuario", usuario);
        return mv;
    }

    /**
     * Editar usuário
     * @param usuario
     * @return redireciona pagina de usuarios
     */
    @PostMapping(value="/editarUsuario/{id}")
    public String editarUsuario(@Valid @ModelAttribute Usuario usuario){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }


    /**
     * Direciona para pagina de calculo de digito unico
     * @return pagina destino
     */
    @GetMapping(path = {"/calcularDigitoUnico/{id}"})
    public ModelAndView getCalcularDigitoUnicoForm(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "calcularDigitoUnico");
        mv.addObject("usuarioID", id);
        return mv;
    }


    /**
     *
     * @param calculo
     * @param usuarioID Identificação do usuário
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping(value="/calcularDigitoUnico/{id}")
    public ModelAndView calcularUnicoDigito(@Valid @ModelAttribute Calculo calculo, @PathVariable("id") Long usuarioID, BindingResult result, RedirectAttributes attributes){

        calculo = desafioService.processarCalculo(calculo,usuarioID);

        ModelAndView mv = new ModelAndView( "sucessoCalculo");
        mv.addObject("usuarioID", usuarioID);
        mv.addObject("digitoCalculado", "O digito único calculado foi: " + calculo.getDigitoUnico());
        return mv;

    }

    /**
     *
     * @param usuario
     * @return
     */
    @ApiOperation(value="Inserir cadastro do usuário")
    @PostMapping(value="/newUsuarioAPI")
    public ResponseEntity<Object> salvarUsuarioAPI(@Valid @RequestBody Usuario usuario){
        return desafioService.saveUserAPI(usuario, false);
    }

    /**
     *
     * @param usuario
     * @return
     */
    @ApiOperation(value="Editar cadastro do usuário")
    @PostMapping(value="/editarUsuarioAPI/{id}")
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
    @PostMapping(value="/calcularDigitoUnicoAPI/{id}")
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

