package com.spring.desafioInter.controller;

import com.spring.desafioInter.model.Calculo;
import com.spring.desafioInter.model.Usuario;
import com.spring.desafioInter.service.DesafioService;
import com.spring.desafioInter.util.CacheCalculos;
import com.spring.desafioInter.util.CalcularDigitoUnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/usuarios", method = RequestMethod.GET)
    public ModelAndView getUsuarios(){
        ModelAndView mv = new ModelAndView( "usuarios");
        List<Usuario> usuarios = desafioService.finAllUsers();
        mv.addObject("usuarios", usuarios);
        return mv;
    }

    @RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET)
    public ModelAndView getDetalhesUsuario(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "detalhesUsuario");
        Usuario usuario = desafioService.findUserById(id);
        mv.addObject("usuario", usuario);
        return mv;
    }

    @RequestMapping( value = "/newUsuario", method = RequestMethod.GET)
    public String getInserirUsuarioForm(){
        return "inserirUsuario";
    }

    @RequestMapping( value = "/newUsuario", method = RequestMethod.POST)
    public String salvarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }

    @RequestMapping(value = "/excluirUsuario/{id}", method = RequestMethod.GET)
    public String getExcluirUsuario(@PathVariable("id") Long id){
        desafioService.deleteUser(id);
        return "redirect:/usuarios";
    }

    @RequestMapping(value = "/editarUsuario/{id}", method = RequestMethod.GET)
    public ModelAndView getEditarUsuario(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "editarUsuario");
        Usuario usuario = desafioService.findUserById(id);
        mv.addObject("usuario", usuario);
        return mv;
    }

    @RequestMapping(value = "/editarUsuario/{id}", method = RequestMethod.POST)
    public String editarUsuario(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes){
        desafioService.saveUser(usuario);
        return "redirect:/usuarios";
    }


    @RequestMapping(value = "/calcularDigitoUnico/{id}", method = RequestMethod.GET)
    public ModelAndView getCalcularDigitoUnicoForm(@PathVariable("id") Long id){
        ModelAndView mv = new ModelAndView( "calcularDigitoUnico");
        mv.addObject("usuarioID", id);
        return mv;
    }

    @RequestMapping(value = "/calcularDigitoUnico/{id}", method = RequestMethod.POST)
    public ModelAndView calcularUnicoDigito(@Valid Calculo calculo, Long usuarioID, BindingResult result, RedirectAttributes attributes){

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

        ModelAndView mv = new ModelAndView( "sucessoCalculo");
        mv.addObject("usuarioID", usuarioID);
        mv.addObject("digitoCalculado", "O digito único calculado foi: " + calculo.getDigitoUnico());
        return mv;

    }


}

