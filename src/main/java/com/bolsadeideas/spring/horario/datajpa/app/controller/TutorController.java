package com.bolsadeideas.spring.horario.datajpa.app.controller;

import com.bolsadeideas.spring.horario.datajpa.app.dao.DirigeDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUploadService;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import com.bolsadeideas.spring.horario.datajpa.app.service.UploadFileDocum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Secured("ROLE_TUTOR")
@Controller
@RequestMapping("/tutor")
@SessionAttributes("usuario")
public class TutorController {

    @Autowired
    private IUsuarioService tutor;

    @Autowired
    private DirigeDao dirige;

    @Autowired
    private IUploadService upload;

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
    public String proyectos(Model model,Principal principal){
        Tutor tut=this.tutor.getUsuarioById(principal.getName()).getTutor();
        try{

            List<Proyecto> proyectos=dirige.getByTutor(principal.getName()).stream().map(ele -> ele.getProyecto()).collect(Collectors.toList());
            model.addAttribute("nombre",(tut.getUsuario().getNombres()+" "+tut.getUsuario().getApellidos()).toUpperCase());
            model.addAttribute("propuestas",proyectos);


        }catch(Exception e){
            model.addAttribute("propuestas",null);
        }

        model.addAttribute("titulo","Mis proyectos");

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

    @RequestMapping("/ver-propuesta/{filename}")
    @ResponseBody
    public void shows(@PathVariable String filename, HttpServletResponse response) {

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        response.setHeader("Content-Disposition", "attachment: filename="+filename);
        response.setHeader("Content-Transfer-Encoding", "binary");

        try {
            BufferedOutputStream bos= new BufferedOutputStream(response.getOutputStream());
            FileInputStream file =new FileInputStream(this.upload.getPath(filename).toString());
            byte [] buf=new byte[1024];
            int len;
            while((len=file.read(buf))>0) {
                bos.write(buf, 0, len);
            }

            bos.close();
            response.flushBuffer();

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error");
            e.printStackTrace();
        }
    }

}
