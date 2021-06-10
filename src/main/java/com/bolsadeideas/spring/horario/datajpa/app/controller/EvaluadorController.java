package com.bolsadeideas.spring.horario.datajpa.app.controller;


import com.bolsadeideas.spring.horario.datajpa.app.dao.AsigandoDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.DirigeDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Asiganado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Calificado;
import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import javax.validation.Valid;


@Secured("ROLE_EVALUADOR")
@Controller
@RequestMapping("/evaluador")
@SessionAttributes("calificado")
public class EvaluadorController {



	@Autowired
	private AsigandoDao asignados;
	
    @Autowired
    private IUsuarioService service;
   
    @Autowired
    private DirigeDao dirigeDao;

    @GetMapping({"/index","","/"})
    public String info(Principal principal, Model model){
        Usuario user=service.getUsuarioById(principal.getName());
        model.addAttribute("titulo","informacion del calificador");
        model.addAttribute("estudiante",user);
        model.addAttribute("infoe","");
        model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());

        return "calificador/info";
    }
    
    @GetMapping("/asignados")
    public String asiganado(Model model, Principal principal) {
    	 Usuario user=service.getUsuarioById(principal.getName());
    	 
    	 List<Proyecto> asignados=this.asignados.getByestadoAndEvaludor(principal.getName(), true).stream().map(
    			 e -> e.getProyecto()).collect(Collectors.toList());
    	 
    	 model.addAttribute("propuestas",asignados);
    	 model.addAttribute("asignados", "");
         model.addAttribute("titulo","proyectos asignados");
         model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
    	
    	
    	return "calificador/asignados";
    }
    
    @GetMapping("/edit")
    public String editInfo(Principal principal,Model model) {
    	
    	Usuario user=this.service.getUsuarioById(principal.getName());
		model.addAttribute("titulo","Editar informacion");
		model.addAttribute("usuario", user);
		model.addAttribute("editEvaluador","");
		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
		return "admin/edit";
    	
    }
    
    @PostMapping("/edit")
    public String saveEdit(Model model,Principal principal,@Valid Usuario usuario,BindingResult result, RedirectAttributes flash,SessionStatus status) {
    	
    	Usuario user=this.service.getUsuarioById(principal.getName());
		if(result.hasErrors()) {
			
			model.addAttribute("mensaje", "Agregar un Director");
			model.addAttribute("editEvaluador","");
			model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
			return "admin/edit";
		}
		
		try {
			
			this.service.save(usuario);
			flash.addFlashAttribute("success", "datos actualizados correctamente");
			status.setComplete();
			return "redirect:/evaluador/";
			
		}catch (Exception e) {
			flash.addFlashAttribute("error", "no se pudo actualizar");
			status.setComplete();
			return "redirect:/evaluador/";
		}
    }
    
    
    @GetMapping("/calificar/{idProyecto}")
    public String calificarProyecto(@PathVariable int idProyecto,Model model, Principal principal) {
    	Calificado calificado=new Calificado();
    	 Usuario user=service.getUsuarioById(principal.getName());
    	 Asiganado modificar=this.asignados.getByEstadoAndEvaluadorAndIdProyecto(principal.getName(),true, idProyecto);
    	 
    	 if(modificar==null) {
    		 return "redirect:/evaluador/asignados";
    	 }
    	 
    	 
    	 model.addAttribute("tituloProyecto","Calificacion del anteproyecto "+modificar.getProyecto().getTitulo());
    	 model.addAttribute("calificado", calificado);
         model.addAttribute("titulo","calificacion proyecto");
         model.addAttribute("idProyecto", modificar.getProyecto().getIdProyecto());
         model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
    	
    	return "calificador/calificacion";
    }
    
    
    @GetMapping("/infopropuesta/{idPropuesta}")
    public String infoPropuesta(Model model,Principal principal,@PathVariable int idPropuesta) {
    	Asiganado asignado=this.asignados.getByEstadoAndEvaluadorAndIdProyecto(principal.getName(), true, idPropuesta);
    	
    	if(asignado==null) {
    		return "redirect:/evaluador/asigandos";
    	}
    	
    	Proyecto pro=asignado.getProyecto();
    	
    	Usuario estudiante=this.service.getUsuarioById(principal.getName());
		
		try{
			Tutor tut=this.dirigeDao.getByPropuesta(pro.getIdProyecto()).getTutor();
			model.addAttribute("tutor",tut);
		}catch (Exception e){
			model.addAttribute("tutor",null);
		}

		System.out.println("Entro al metodo");
		model.addAttribute("propuesta", pro);

		model.addAttribute("infop","");
		model.addAttribute("nombre", (estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Informacion de la propuesta asignada");
    	
    	return "estudiante/info-propuesta";
    }
    
    
    @PostMapping("/calificar")
    public String calificar(@Valid Calificado calificado, BindingResult result,@RequestParam int idProyecto,Principal principal,Model model){
    
    	 Usuario user=service.getUsuarioById(principal.getName());
    	 Asiganado modificar=this.asignados.getByEstadoAndEvaluadorAndIdProyecto(principal.getName(),true, idProyecto);
    	
    	
    	 
    	 if(modificar==null) {
    		 return "redirect:/evaluador/asignados";
    	 }
    	 model.addAttribute("tituloProyecto","Calificacion del anteproyecto "+modificar.getProyecto().getTitulo());
    	 model.addAttribute("calificado", calificado);
         model.addAttribute("titulo","calificacion proyecto");
         model.addAttribute("idProyecto", modificar.getProyecto().getIdProyecto());
         model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
         
    	if(result.hasErrors()) {
    		for(FieldError e:result.getFieldErrors()) {
    			System.out.println(e.getField()+" "+e.getDefaultMessage());
    		}
    		
    		return "calificador/calificacion";
    	}
    	calificado.setFecha(new Date());
    	calificado.setTutor(user.getEvaluador());
    	
    	double cal70=(calificado.getTitulo()*0.05)+(calificado.getPlanteamiento()*0.2)+(calificado.getJustificacion()*0.15)
    			+(calificado.getObjetivos()*0.2) +(calificado.getAlcances()*0.05)+(calificado.getMarcoteorico()*0.1)
    			+(calificado.getDisenoMetodologico()*0.1)+(calificado.getPreosupuestoYcronograma()*0.05)
    			+(calificado.getReferencias()*0.1);
    	
    	double cal30=(calificado.getCumplimineto()*0.5) +(calificado.getRedacion()*0.5);
    	
    	
    	
    	DecimalFormat format=new DecimalFormat();
    	format.setMaximumIntegerDigits(2);
    	
    	calificado.setNota((cal70*0.7)+(cal30*0.3));
    	
    
    	return "redirect:/evaluador/calificar/"+idProyecto;
    	
    }
}
