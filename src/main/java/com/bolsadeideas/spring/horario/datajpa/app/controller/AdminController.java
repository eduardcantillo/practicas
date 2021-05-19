package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.security.Principal;
import java.util.List;

import com.bolsadeideas.spring.horario.datajpa.app.dao.ProyectoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.TutorDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Secured("ROLE_ADMINISTRADOR")
@Controller
@RequestMapping("/admin")
@SessionAttributes("usuario")
public class AdminController {

	@Autowired
	private IUsuarioService user;

	@Autowired
	private TutorDao tutorDao;

	@Autowired
	private ProyectoDao proyectoDao;

	@GetMapping({"/blank","/",""})
	public String Index(Principal pri,Model model) {
		Usuario usuario=this.user.getUsuarioById(pri.getName());
		System.out.println(usuario);
		model.addAttribute("usuario",usuario);
		
		return "admin/blank";
	}

	@GetMapping("/profesores-activos")
	public String profesoresActivos(Model model,Principal principal) {
		Usuario user=this.user.getUsuarioById(principal.getName());

		List<Usuario> activos=this.user.getTutores((byte) 1);

		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Listado de profesores Activos");
		model.addAttribute("profesor", activos);
		return "admin/listados-Pactivos";
	}

	@GetMapping("deshabilitar/{id}")
	public String deshabilitar(@PathVariable String id, RedirectAttributes flask){
		Usuario user=this.user.getUsuarioById(id);
		if (user == null) {
			flask.addFlashAttribute("mensaje","no se encontro el director en la base de datos");
			return "redirect:/admin/profesores-activos";
		}else if(!user.getRol().equals("ROLE_TUTOR")){
			flask.addFlashAttribute("mensaje","El suauario no es un tutor");
			return "redirect:/admin/profesores-activos";
		}

		user.setHabilitado((byte)0);
		this. user.save(user);

		return "redirect:/admin/profesores-inactivos";
	}


	@GetMapping("habilitar/{id}")
	public String habilitar(@PathVariable String id, RedirectAttributes flask){
		Usuario user=this.user.getUsuarioById(id);
		if (user == null) {
			flask.addFlashAttribute("mensaje","no se encontro el director en la base de datos");
			return "redirect:/admin/profesores-activos";
		}else if(!user.getRol().equals("ROLE_TUTOR")){
			flask.addFlashAttribute("mensaje","El suauario no es un tutor");
			return "redirect:/admin/profesores-activos";
		}

		user.setHabilitado((byte)1);
		this. user.save(user);

		return "redirect:/admin/profesores-activos";
	}

	@GetMapping("/profesores-inactivos")
	public String profesoresInactivos(Model model,Principal principal) {
		List<Usuario> inactivos=this.user.getTutores((byte) 0);
		Usuario user=this.user.getUsuarioById(principal.getName());

		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Listado de profesores Inactivos");
		model.addAttribute("profesor", inactivos);
		return "admin/listados-Pinactivos";
		
	}

	@GetMapping("/proyectos-espera")
	public String proyectosEspera(Model model, Principal principal){
		Usuario user=this.user.getUsuarioById(principal.getName());
		List<Proyecto> activos=this.proyectoDao.findByEstado("ESPERA");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Listado de proyectos en espera");

		return "";
	}
	
	@GetMapping("/proyecto-en-espera")
	public String ProyectoEspera(Model model) {
		model.addAttribute("titulo", "Listado de profesores Inactivos");
		
		
		model.addAttribute("profesor", true);
		return "admin/proyecto-espera";
		
	}
}
