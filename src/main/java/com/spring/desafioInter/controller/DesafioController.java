package com.spring.desafioInter.controller;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.service.DesafioService;
import com.spring.desafioInter.util.CacheCalculos;
import com.spring.desafioInter.util.CalcularDigitoUnico;
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

@Controller
public class DesafioController {

    @Autowired
    DesafioService desafioService;

    CacheCalculos cache = new CacheCalculos();

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    /**
     * Obtém lista de usuários
     * @return ModelAndView
     */
    @RequestMapping(value = "/usuarios", method = RequestMethod.GET)
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
    @RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET)
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
    @RequestMapping( value = "/newUsuario", method = RequestMethod.GET)
    public String getInserirUsuarioForm(){
        return "inserirUsuario";
    }

    /**
     * Insere novo usuário
     * @param usuario
     * @return redireciona pagina de usuarios
     */
    @RequestMapping( value = "/newUsuario", method = RequestMethod.POST)
    public String salvarUsuario(@Valid @ModelAttribute Usuario usuario){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Escluir usuário
     * @param id Identificação do usuário
     * @return redireciona pagina de usuarios
     */
    @RequestMapping(value = "/excluirUsuario/{id}", method = RequestMethod.GET)
    public String getExcluirUsuario(@PathVariable("id") Long id){
        desafioService.deleteUser(id);
        return "redirect:/usuarios";
    }

    /**
     * Direciona para pagina de editar cadastro de usuário
     * @return pagina destino
     */
    @RequestMapping(value = "/editarUsuario/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/editarUsuario/{id}", method = RequestMethod.POST)
    public String editarUsuario(@Valid @ModelAttribute Usuario usuario){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }


    /**
     * Direciona para pagina de calculo de digito unico
     * @return pagina destino
     */
    @RequestMapping(value = "/calcularDigitoUnico/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/calcularDigitoUnico/{id}", method = RequestMethod.POST)
    public ModelAndView calcularUnicoDigito(@Valid @ModelAttribute Calculo calculo, @PathVariable("id") Long usuarioID, BindingResult result, RedirectAttributes attributes){

        calculo = processarCalculo(calculo,usuarioID);

        ModelAndView mv = new ModelAndView( "sucessoCalculo");
        mv.addObject("usuarioID", usuarioID);
        mv.addObject("digitoCalculado", "O digito único calculado foi: " + calculo.getDigitoUnico());
        return mv;

    }

    /**
     * Valida se usuario esta populado
     * @param usuario
     * @param isEditar
     * @return
     */
    private boolean isUsuarioOK(Usuario usuario, boolean isEditar){

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
    private ResponseEntity<Object> saveUserAPI(Usuario usuario, boolean isEditar){
        if (isUsuarioOK(usuario, isEditar)) {
            desafioService.saveUser(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Request Invalido",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Processa o calculo do digito unico
     * @param calculo
     * @param usuarioID
     * @return
     */
    private Calculo processarCalculo(Calculo calculo, Long usuarioID){

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

        Usuario usuario = desafioService.findUserById(usuarioID);
        calculo.setUsuario(usuario);

        desafioService.saveCalculo(calculo);

        return  calculo;
    }


    /**
     *
     * @param usuario
     * @return
     */
    @RequestMapping( value = "/newUsuarioAPI", method = RequestMethod.POST)
    public ResponseEntity<Object> salvarUsuarioAPI(@Valid @RequestBody Usuario usuario){
        return saveUserAPI(usuario, false);
    }

    /**
     *
     * @param usuario
     * @return
     */
    @RequestMapping(value = "/editarUsuarioAPI/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> editarUsuarioAPI(@Valid @RequestBody Usuario usuario){
        return saveUserAPI(usuario, true);
    }

    /**
     * 
     * @param calculo
     * @param usuarioID
     * @return
     */
    @RequestMapping(value = "/calcularDigitoUnicoAPI/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> calcularUnicoDigito(@Valid @RequestBody Calculo calculo, @PathVariable("id") Long usuarioID){

        calculo = processarCalculo(calculo,usuarioID);
        return new ResponseEntity<>("O digito único calculado foi: " + calculo.getDigitoUnico(), HttpStatus.OK);
    }





}

