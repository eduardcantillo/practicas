package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;

@Secured("ROLE_ESTUDIANTE")
@Controller
@RequestMapping("/admin")
@SessionAttributes("usuario")
public class AdminController {

	@Autowired
	private IUsuarioService user;
	
	
	@GetMapping({"/blank","/",""})
	public String Index(Principal pri,Model model) {
		Usuario usuario=this.user.getUsuarioById(pri.getName());
		System.out.println(usuario);
		model.addAttribute("usuario",usuario);
		
		return "admin/blank";
	}

	@GetMapping("/profesoresActivos")
	public String profesoresActivos(Model model,Usuario usuario) {
		System.out.println(usuario);
		model.addAttribute("titulo", "Listado de profesores Activos");
		model.addAttribute("profesor", true);
		return "admin/listados-Pactivos";
	}
	
	@GetMapping("/profesoresInactivos")
	public String profesoresInactivos(Model model) {
		model.addAttribute("titulo", "Listado de profesores Inactivos");
		model.addAttribute("profesor", true);
		return "admin/listados-Pinactivos";
		
	}
	
	@GetMapping("/proyecto-en-espera")
	public String ProyectoEspera(Model model) {
		model.addAttribute("titulo", "Listado de profesores Inactivos");
		
		
		model.addAttribute("profesor", true);
		return "admin/proyecto-espera";
		
	}
}
