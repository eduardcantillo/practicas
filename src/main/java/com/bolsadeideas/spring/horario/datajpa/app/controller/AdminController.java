package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.bolsadeideas.spring.horario.datajpa.app.dao.AsigandoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.EstadoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.EvaluadorDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.ProyectoDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Asiganado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Estado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Evaluador;
import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
	private ProyectoDao proyectoDao;

	@Autowired
	private EstadoDao estado;
	
	@Autowired
	private AsigandoDao asignar;
	
	@Autowired
	private EvaluadorDao evaluador;

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

		model.addAttribute("propuestas",activos);
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Listado de proyectos en espera");

		return "admin/proyecto-espera";
	}

	@GetMapping("/aprobar/{id}")
	public String asignarEvaluador(Model model,Principal principal,@PathVariable int id){

		Usuario user=this.user.getUsuarioById(principal.getName());
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("id",id);
		return "admin/asignar";
	}

	@PostMapping("/asignar/{id}")
	public String agregarEvaluadores(@PathVariable int id,@RequestParam String codigo1,@RequestParam String codigo2,@RequestParam String codigo3,RedirectAttributes flash,Model model){

		Proyecto pro=this.proyectoDao.findById(id).orElse(null);
		
		Evaluador eval1,eval2,eval3;
		Asiganado asig1=new Asiganado();
		Asiganado asig2=new Asiganado();
		Asiganado asig3=new Asiganado();
		

		if(pro==null){
			flash.addFlashAttribute("error", "El proyecto a aprobar no se encuentra en la bd");
			return "redirect:/admin/proyectos-espera";
		}
		if(codigo1.isBlank() || codigo2.isBlank()) {
			model.addAttribute("mensaje", "por favor rellene por lo menos el capo 1 y 2");
			return "admin/asignar";
		}
		
		eval1=this.evaluador.findById(codigo1).orElse(null);
		eval2=this.evaluador.findById(codigo2).orElse(null);
		
		if(eval1==null || eval2==null) {
			model.addAttribute("mensaje", "El codigo digitado en el campo 1 o 2 no corresponde a ningun evaluador");
			return "admin/asignar";
		}
		
		if(!codigo3.isBlank()) {
			eval3=this.evaluador.findById(codigo3).orElse(null);
			
			if(eval3==null) {
				model.addAttribute("mensaje", "no se encontro ningun el valuador con los datos de campo 3");
				return "admin/asignar";
			}
			
			asig3.setCalificable(true);
			asig3.setEvaluador(eval3);
			asig3.setFecha(new Date());
			asig3.setProyecto(pro);
			
			this.asignar.save(asig3);
		}
		
	asig1.setCalificable(true);
	asig1.setEvaluador(eval1);
	asig1.setFecha(new Date());
	asig1.setProyecto(pro);
	
	asig2.setCalificable(true);
	asig2.setEvaluador(eval2);
	asig2.setFecha(new Date());
	asig2.setProyecto(pro);
	 
	this.asignar.save(asig1);
	this.asignar.save(asig2);
	
	Estado es=this.estado.findById(2).orElse(null);
	
	pro.setEstado(es);
	this.proyectoDao.save(pro);
		
		model.addAttribute("mensaje", "todo correcto hasta aqui");
		return "redirect:/admin/proyectos-espera";
	}

	@GetMapping("/proyectos-aprobados")
	public String ProyectoEspera(Model model,Principal principal) {
		model.addAttribute("titulo", "Listado de los proyectos Aprobados");
		model.addAttribute("aprobados", true);
		Usuario user=this.user.getUsuarioById(principal.getName());
		List<Proyecto> activos=this.proyectoDao.findByEstado("APROBADOS");

		model.addAttribute("propuestas",activos);
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		

		return "admin/proyecto-espera";
		
	}
}
