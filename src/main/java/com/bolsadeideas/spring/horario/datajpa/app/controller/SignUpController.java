package com.bolsadeideas.spring.horario.datajpa.app.controller;




import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.spring.horario.datajpa.app.models.Estudiante;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;




@Controller()
@RequestMapping("/sign-up")
@SessionAttributes("usuario")
public class SignUpController {

	@Autowired
	private IUsuarioService user;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/student")
	public String getSingUp(Model model) {
		model.addAttribute("titulo", "Registro de estudiante");
		Usuario usuario=new Usuario();
		model.addAttribute("usuario",usuario);
		return "register";
	}
	
	@PostMapping("/student")
	public String guardar(@RequestParam int semestre,@RequestParam double promedio,@Valid Usuario usuario,BindingResult result,SessionStatus status,Model model) {
		
		
		System.out.println("Entro al metodo");
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Registro de estudiante");
			return "register";
		}
		
		
		if(this.user.getUsuarioByEmail(usuario.getEmail())!=null || this.user.getUsuarioById(usuario.getCodigo())!= null) {
			model.addAttribute("titulo", "Registro de estudiante");
			model.addAttribute("error", "erro codigo o email ya utilizado");
			return "register";
		}
		
	
		System.out.println(semestre+ " "+ promedio);
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuario.setHabilitado((byte)1);
		usuario.setRol("ROLE_ESTUDIANTE");
		
		System.out.println(usuario.getEmail());
		Estudiante es=new Estudiante();
		
		es.setPromedio(promedio);
		es.setSemestre(""+semestre);
		es.setIdEstudiantes(usuario.getCodigo());
		this.user.save(usuario);
		this.user.saveEstudent(es);
		
		status.setComplete();
		return "redirect:/login";
	}
}
