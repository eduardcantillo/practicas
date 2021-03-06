package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import com.bolsadeideas.spring.horario.datajpa.app.dao.AsigandoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.DirigeDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.EstadoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.EvaluadorDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.ProyectoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.TutorDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.UsuarioDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Asiganado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Estado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Evaluador;
import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Secured("ROLE_ADMINISTRADOR")
@Controller
@RequestMapping("/admin")
@SessionAttributes("usuario")
public class AdminController {

	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private IUsuarioService user;

	@Autowired
	private ProyectoDao proyectoDao;

	@Autowired
	private EstadoDao estado;
	
	@Autowired
	private AsigandoDao asignar;
	
	@Autowired
	private TutorDao tutorDao;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private EvaluadorDao evaluador;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private DirigeDao dirigeDao;

	
	

	@GetMapping("/profesores-activos")
	public String profesoresActivos(Model model,Principal principal) {
		Usuario user=this.user.getUsuarioById(principal.getName());

		List<Usuario> activos=this.user.getTutores((byte) 1);

		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Listado de profesores Activos");
		model.addAttribute("profesor", activos);
		return "admin/listados-Pactivos";
	}

	
	 public void sendEmail(String recipientEmail,String nombre,String email,String estado)
	            throws MessagingException, UnsupportedEncodingException {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);

	        helper.setFrom("trabajosdegrados@ufps.edu.co", "Portal para los tranajos de grados");
	        helper.setTo(recipientEmail);

	        String subject = "Cambio de contrase??a contrase??a";

	        String content = "<p>Hola, "+nombre+"</p>"
	                + "<p>Tu cuenta en la plataforma de para de control de .</p>"
	                + "<p>Los proyectos de grado acaba de ser:</p>"
	                + "<p>"+estado+"</p>"
	                + "<br>"
	                + "<p>Para mas informacion comuniquese al siguiente correo, "
	                + email+"</p>";

	        helper.setSubject(subject);

	        helper.setText(content, true);

	        mailSender.send(message);
	    }
	
	@GetMapping("deshabilitar/{id}")
	public String deshabilitar(@PathVariable String id, RedirectAttributes flask,Principal principal){
		Usuario user=this.user.getUsuarioById(id);
		Usuario admin=this.user.getUsuarioById(principal.getName());
		if (user == null) {
			flask.addFlashAttribute("mensaje","no se encontro el director en la base de datos");
			return "redirect:/admin/profesores-activos";
		}else if(!user.getRol().equals("ROLE_TUTOR")){
			flask.addFlashAttribute("mensaje","El suauario no es un tutor");
			return "redirect:/admin/profesores-activos";
		}

		user.setHabilitado((byte)0);
		try {
			sendEmail(user.getEmail(),user.getNombres(),admin.getEmail(),"Desactivada");
			this. user.save(user);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return "redirect:/admin/profesores-inactivos";
	}


	@GetMapping("habilitar/{id}")
	public String habilitar(@PathVariable String id, RedirectAttributes flask,Principal principal){
		Usuario user=this.user.getUsuarioById(id);
		Usuario admin=this.user.getUsuarioById(principal.getName());
		if (user == null) {
			flask.addFlashAttribute("mensaje","no se encontro el director en la base de datos");
			return "redirect:/admin/profesores-activos";
		}else if(!user.getRol().equals("ROLE_TUTOR")){
			flask.addFlashAttribute("mensaje","El suauario no es un tutor");
			return "redirect:/admin/profesores-activos";
		}

		user.setHabilitado((byte)1);
		try {
			sendEmail(user.getEmail(),user.getNombres(),admin.getNombres(),"Activada");
			this. user.save(user);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

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

	@GetMapping({"/proyectos-aprobados","","/"})
	public String ProyectoEspera(Model model,Principal principal) {
		model.addAttribute("titulo", "Listado de los proyectos Aprobados");
		Usuario user=this.user.getUsuarioById(principal.getName());
		List<Proyecto> activos=this.proyectoDao.findByTwoState("APROBADOS","CORRECCION");
		model.addAttribute("aprobados","");
		model.addAttribute("propuestas",activos);
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		

		return "admin/proyecto-espera";
		
	}
	
	@GetMapping("/proyectos-finalizados")
	public String ProyectosFinalizados(Model model,Principal principal) {
		model.addAttribute("titulo", "Listado de los proyectos finalizados");
		Usuario user=this.user.getUsuarioById(principal.getName());
		List<Proyecto> activos=this.proyectoDao.findByEstado("ACCEPTADOS");
		model.addAttribute("finalizados","");
		model.addAttribute("propuestas",activos);
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		

		return "admin/proyecto-espera2";
		
	}
	
	@GetMapping("/add/admin")
	public String AddAdmin(Model model,Principal principal) {
		
		Usuario usuario=new Usuario();
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		model.addAttribute("url","admin");
		model.addAttribute("usuario",usuario);
		model.addAttribute("mensaje", "Agregar un administrador");
		model.addAttribute("add","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		
		return "admin/registrar";
	}
	
	@PostMapping("/add/admin")
	public String saveAdmin(Model model, @Valid Usuario usuario, BindingResult result,Principal principal) {
		
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		if(result.hasErrors()) {
			model.addAttribute("mensaje", "Agregar un administrador");
			model.addAttribute("url","admin");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user1=this.usuarioDao.findById(usuario.getCodigo()).orElse(null);
		
		if(user1!=null) {
			model.addAttribute("mensaje", "Agregar un administrador");
			model.addAttribute("url","admin");
			model.addAttribute("error", "ya existe un uasuario con el codigo digitado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user2=this.usuarioDao.getByEmail(usuario.getEmail());
		
		if(user2!=null) {
			model.addAttribute("mensaje", "Agregar un administrador");
			model.addAttribute("url","admin");
			model.addAttribute("error", "ya existe un uasuario con el email agrgado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuario.setRol("ROLE_ADMINISTRADOR");
		
		this.usuarioDao.save(usuario);
		
		return "redirect:/admin/proyectos-espera";
	}
	
	@GetMapping("/add/director")
	public String AddDirector(Model model,Principal principal) {
		
		Usuario usuario=new Usuario();
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		model.addAttribute("url","director");
		model.addAttribute("usuario",usuario);
		model.addAttribute("mensaje", "Agregar un director");
		model.addAttribute("add","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		
		return "admin/registrar";
	}
	
	@GetMapping("/add/evaluador")
	public String AddEvaluador(Model model,Principal principal) {
		
		Usuario usuario=new Usuario();
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		model.addAttribute("url","evaluador");
		model.addAttribute("usuario",usuario);
		model.addAttribute("mensaje", "Agregar un Evaluador");
		model.addAttribute("add","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		
		return "admin/registrar";
	}
	
	@PostMapping("/add/evaluador")
	public String saveEvaluador(Model model, @Valid Usuario usuario, BindingResult result,Principal principal) {
		
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		if(result.hasErrors()) {
			model.addAttribute("mensaje", "Agregar un evaluador");
			model.addAttribute("url","evaluador");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user1=this.usuarioDao.findById(usuario.getCodigo()).orElse(null);
		
		if(user1!=null) {
			model.addAttribute("mensaje", "Agregar un evaluador");
			model.addAttribute("url","evaluador");
			model.addAttribute("error", "ya existe un uasuario con el codigo digitado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user2=this.usuarioDao.getByEmail(usuario.getEmail());
		
		if(user2!=null) {
			model.addAttribute("mensaje", "Agregar un evaluador");
			model.addAttribute("url","admin");
			model.addAttribute("error", "ya existe un uasuario con el email agregado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		usuario.setHabilitado((byte) 1);
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuario.setRol("ROLE_EVALUADOR");
		Evaluador evaluador=new Evaluador();
		evaluador.setIdEvaluador(usuario.getCodigo());
		this.usuarioDao.save(usuario);
		this.evaluador.save(evaluador);
		return "redirect:/admin/proyectos-espera";
	}
	
	@PostMapping("/add/director")
	public String saveTutor(Model model, @Valid Usuario usuario, BindingResult result,Principal principal) {
		
		Usuario user=this.user.getUsuarioById(principal.getName());
		
		if(result.hasErrors()) {
			model.addAttribute("mensaje", "Agregar un Director");
			model.addAttribute("url","evaluador");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user1=this.usuarioDao.findById(usuario.getCodigo()).orElse(null);
		
		if(user1!=null) {
			model.addAttribute("mensaje", "Agregar un director");
			model.addAttribute("url","evaluador");
			model.addAttribute("error", "ya existe un uasuario con el codigo digitado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		Usuario user2=this.usuarioDao.getByEmail(usuario.getEmail());
		
		if(user2!=null) {
			model.addAttribute("mensaje", "Agregar un director");
			model.addAttribute("url","admin");
			model.addAttribute("error", "ya existe un uasuario con el email agregado");
			model.addAttribute("add","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/registrar";
		}
		
		usuario.setHabilitado((byte) 1);
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuario.setHabilitado((byte) 0);
		usuario.setRol("ROLE_TUTOR");
		Tutor evaluador=new Tutor();
		evaluador.setIdTutor(usuario.getCodigo());
		this.usuarioDao.save(usuario);
		this.tutorDao.save(evaluador);
		return "redirect:/admin/proyectos-espera";
	}
	
	@GetMapping("/info")
	public String info(Model model,Principal principal) {
		
		Usuario user=this.user.getUsuarioById(principal.getName());
		model.addAttribute("mensaje", "Agregar un Director");
		model.addAttribute("estudiante", user);
		model.addAttribute("info","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		
		return "admin/info";
		
	}
	@GetMapping("/edit")
	public String edit(Model model, Principal principal) {

		Usuario user=this.user.getUsuarioById(principal.getName());
		model.addAttribute("mensaje", "Agregar un Director");
		model.addAttribute("usuario", user);
		model.addAttribute("edit","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		return "admin/edit";
	}
	
	@PostMapping("/edit")
	public String saveEdicion(Model model, Principal principal,@Valid Usuario usuario,BindingResult result,SessionStatus status,RedirectAttributes flash){
		Usuario user=this.user.getUsuarioById(principal.getName());
		if(result.hasErrors()) {
			
			model.addAttribute("mensaje", "Agregar un Director");
			model.addAttribute("edit","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/edit";
		}
		
		try {
			
			this.user.save(usuario);
			flash.addFlashAttribute("success", "datos actualizados correctamente");
			status.setComplete();
			return "redirect:/admin/info";
			
		}catch (Exception e) {
			flash.addFlashAttribute("error", "no se pudo actualizar");
			status.setComplete();
			return "redirect:/admin/info";
		}
		
	}
	
@GetMapping("/verProyecto/{idProyecto}")
public String verProyecto(Model model,Principal principal,@PathVariable int idProyecto) {
	
	Proyecto pro=proyectoDao.findById(idProyecto).orElse(null);
	
	Usuario estudiante=this.user.getUsuarioById(principal.getName());
	
	try{
		Tutor tut=this.dirigeDao.getByPropuesta(pro.getIdProyecto()).getTutor();
		model.addAttribute("tutor",tut);
	}catch (Exception e){
		model.addAttribute("tutor",null);
	}

	System.out.println("Entro al metodo");
	model.addAttribute("propuesta", pro);
	model.addAttribute("propuestas", true);
	model.addAttribute("infop","");
	model.addAttribute("nombre", (estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
	model.addAttribute("titulo", "Informacion de la propuesta asignada");
	
	return "estudiante/info-propuesta";
}

@GetMapping("/propuesta/{idProyecto}/documentos")
public String DocumentosPropuestas(Model model,@PathVariable int idProyecto,Principal principal){
	Usuario estudiante=this.user.getUsuarioById(principal.getName());
	Proyecto pro=this.proyectoDao.findById(idProyecto).orElse(null);

	if(pro==null){
		return "redirect:/admin/";
	}
	model.addAttribute("propuestas", true);
	model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
	model.addAttribute("documentos",pro.getDocumentos());
	model.addAttribute("infop", "");
	return "estudiante/documentos";
}

@GetMapping("/generarReporte")
public String reportes (Model model,Principal principal) {
	
	model.addAttribute("titulo", "Listado de los proyectos Aprobados");
	Usuario user=this.user.getUsuarioById(principal.getName());
	List<Proyecto> activos=(List<Proyecto>)this.proyectoDao.findAll();
	model.addAttribute("reportes","");
	model.addAttribute("propuestas",activos);
	model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
	

	return "admin/proyecto-espera2";
	

}
@PostMapping("/generarReporte")
public String reportesByFecha (Model model,Principal principal,@DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
		@DateTimeFormat(pattern="yyyy-MM-dd")Date fin) {
	
	model.addAttribute("titulo", "Listado de los proyectos Aprobados");
	Usuario user=this.user.getUsuarioById(principal.getName());
	List<Proyecto> activos=this.proyectoDao.entreFecha(inicio, fin);
	model.addAttribute("reportes","");
	model.addAttribute("propuestas",activos);
	model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
	
	return "admin/proyecto-espera2";
	

}


}
