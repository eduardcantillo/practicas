package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(Principal principal,@RequestParam(required=false) String logout,@RequestParam(required=false) String error,Model model) {
		
		
	if(error!=null) {
			model.addAttribute("error", "Codigo o contraseña incorrecto");
		}	
	if(principal==null ) {
		return "login";
	}
	
	
	return "redirect:/";
	}
	
	@GetMapping({"/",""})
	public String redireccionar(Principal principal,Authentication auth) {
		if(auth==null) return "login";

		if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))) {
			System.out.println(principal.getName());
			return "redirect:/estudiante/subir";
		}else if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR"))){
			return "redirect:/tutor";
		}else if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EVALUADOR"))){
			return "redirect:/evaluador";
		}else if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))){
			return "redirect:/admin";
		}


		System.out.println("No entró");
		return "login";
	}
	
	
	
}
