package com.bolsadeideas.spring.horario.datajpa.app.controller;

import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Secured("ROLE_TUTOR")
@Controller
@RequestMapping("/tutor")
public class TutorController {
    @Autowired
    private IUsuarioService tutor;

    @GetMapping("/ver/{id}")
    public String verProyecto(@RequestParam int id, Principal principal, Model model){

        return "";
    }
}
