package com.bolsadeideas.spring.horario.datajpa.app.controller;


import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Secured("ROLE_EVALUADOR")
@Controller
@RequestMapping("/evaluador")
public class EvaluadorController {



    @Autowired
    private IUsuarioService service;

    @GetMapping({"/index","","/"})
    public String info(Principal principal, Model model){
        Usuario user=service.getUsuarioById(principal.getName());
        model.addAttribute("titulo","informacion del calificador");
        model.addAttribute("estudiante",user);
        model.addAttribute("infoe","");
        model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());

        return "calificador/info";
    }
}
