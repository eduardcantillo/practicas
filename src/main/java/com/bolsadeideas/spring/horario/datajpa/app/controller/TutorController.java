package com.bolsadeideas.spring.horario.datajpa.app.controller;

import com.bolsadeideas.spring.horario.datajpa.app.dao.DirigeDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Dirige;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Secured("ROLE_TUTOR")
@Controller
@RequestMapping("/tutor")
@SessionAttributes("usuario")
public class TutorController {

    @Autowired
    private IUsuarioService tutor;

    @Autowired
    private DirigeDao dirige;

    @GetMapping({"/info","","/"})
    public String infoTutor(Principal principal,Model model){
        Usuario tutor= this.tutor.getUsuarioById(principal.getName());
        model.addAttribute("estudiante",tutor);
        model.addAttribute("titulo","informacion del tutor");
        model.addAttribute("personal","info");
        model.addAttribute("nombre",(tutor.getNombres()+" "+tutor.getApellidos()).toUpperCase());
        return "tutor/info-tutor";
    }

    @GetMapping("/edit")
    public String showEditInfo(Principal principal, Model model){

        Usuario tutor= this.tutor.getUsuarioById(principal.getName());
        model.addAttribute("usuario",tutor);
        model.addAttribute("titulo","editar la idnformacion del tutor");
        model.addAttribute("nombre",(tutor.getNombres()+" "+tutor.getApellidos()).toUpperCase());
        model.addAttribute("info","info");
        return "tutor/edit-info";
    }

    @GetMapping("/proyectos")
    public String projectos(Model model,Principal principal){
        Tutor tut=this.tutor.getUsuarioById(principal.getName()).getTutor();
        List<Dirige> proyectos=dirige.getByTutor(principal.getName());
       model.addAttribute("proyectos",proyectos);
        return "tutor/proyectos";
    }

    @PostMapping("/guardar")
    public String guardarEdit(@Valid Usuario usuario, BindingResult result, SessionStatus status,Model model, Principal principal){

        if(result.hasErrors()){
            Usuario tutor= this.tutor.getUsuarioById(principal.getName());
            model.addAttribute("titulo","editar la idnformacion del tutor");
            model.addAttribute("nombre",(tutor.getNombres()+" "+tutor.getApellidos()).toUpperCase());
            model.addAttribute("info","info");

            return "tutor/edit-info";
        }

        this.tutor.save(usuario);

        status.setComplete();
        return "redirect:/tutor/";
    }

    @GetMapping("/proyecto/{id}")
    public String verProyecto(@RequestParam int id, Principal principal, Model model){


        return "";
    }

}
