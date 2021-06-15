package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bolsadeideas.spring.horario.datajpa.app.dao.*;
import com.bolsadeideas.spring.horario.datajpa.app.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.spring.horario.datajpa.app.service.IUploadService;
import com.bolsadeideas.spring.horario.datajpa.app.service.IUsuarioService;
import com.bolsadeideas.spring.horario.datajpa.app.service.UploadFileDocum;

@Secured("ROLE_ESTUDIANTE")
@Controller
@RequestMapping("/estudiante")
@SessionAttributes(value = {"estudiante","proyecto","usuario","documento"})
public class EstudianteController {
	
	@Autowired
	private IUsuarioService user;
	@Autowired
	private TipoProyectoDao tipe;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private EstadoDao estado;

	@Autowired
	private IEstudianteDao estudiante;

	@Autowired
	private IUploadService upload;
	
	@Autowired
	private ProyectoDao proyecto;

	@Autowired
	@Qualifier("documentos")
	private UploadFileDocum oploadDocu;

	@Autowired
	private TutorDao tutor;

	@Autowired
	private DirigeDao dirigeDao;
	@Autowired 
	private DocumentoDao documento;
	
	@Autowired
	private CalificadoDao calificado;
	
	
	
	@GetMapping("/subir")
	public String index(Principal principal,Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		Proyecto pro=this.proyecto.findById(estudiante.getEstudiante().getProyecto()).orElse(null);
		
		if((estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()) && pro==null) {
			System.out.println(estudiante);
			Proyecto proyecto =new Proyecto();
			model.addAttribute("titulo", "subir propuesta");
			model.addAttribute("estudiante", estudiante);
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			return "estudiante/subirPropuesta";
		}
		
		return "redirect:/estudiante/info-propuesta";
		
	}
	
	@GetMapping("/editar-info")
	public String editar(Principal principal,Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("promedio", estudiante.getEstudiante().getPromedio());
		model.addAttribute("semestre", estudiante.getEstudiante().getSemestre());
		model.addAttribute("usuario", estudiante);
		model.addAttribute("info", "");
		
		return "estudiante/edit";
	}

	
	@GetMapping("/see-documento")
	public String verDocumentos(Principal principal, Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		Proyecto pro=this.proyecto.findById(estudiante.getEstudiante().getProyecto()).orElse(null);

		if(pro==null){
			pro=estudiante.getEstudiante().getProyectos().get(0);
		}

		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("documentos",pro.getDocumentos());
		model.addAttribute("infop", "");
		return "estudiante/documentos";
	}
	
	@GetMapping("/add-documento")
	public String mostrarFor(Principal principal,Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		model.addAttribute("estudiante", estudiante);
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		Documento documento=new Documento();
		model.addAttribute("infoe", "");
		model.addAttribute("documento", documento);
		return "estudiante/subir-doc";
	}
	
	@PostMapping("/save-documento")
	public String guardarDocumento(@Valid Documento documento,BindingResult result,Principal principal,Model model,@RequestParam(name = "documento") MultipartFile archivo) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		model.addAttribute("infoe", "");
		if(result.hasErrors()) {
			model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
			return "estudiante/subir-doc";
		}
		
		if(archivo.isEmpty()) {
			
		}
		String arch=archivo.getOriginalFilename();
		System.out.println(arch);
		int index=arch.lastIndexOf(".");
		
		if(index>0) {
			arch=arch.substring(index+1);
		}
		
		if(!arch.equalsIgnoreCase("pdf")) {
		
			model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
			model.addAttribute("document","el archivo debe ser en formato pdf");
			return "estudiante/subir-doc";
		}
		
		String uniqueFile=null;
		try {
			uniqueFile=this.oploadDocu.copy(archivo);
			documento.setUrl(uniqueFile);
		} catch (IOException e) {
			
			model.addAttribute("archivo", "el archivo no se pudo subir");
			return "estudiante/subir-doc";
			}
		Proyecto pro=this.proyecto.findById(estudiante.getEstudiante().getProyecto()).orElse(null);

		if(pro==null){
			pro=estudiante.getEstudiante().getProyectos().get(0);
		}

		documento.setProyecto(pro);
		this.documento.save(documento);
		
		
		
		return "redirect:/estudiante/see-documento";
	}

	@GetMapping("/agregar-companero")
	public String addCompañero(Model model,Principal principal){
		Usuario user=this.user.getUsuarioById(principal.getName());
		Proyecto proyecto1 = this.proyecto.findById(user.getEstudiante().getProyecto()).orElse(null);

		if((user.getEstudiante().getProyectos()==null || user.getEstudiante().getProyectos().isEmpty()) && proyecto1==null ){
			return "redirect:/estudiante/subir";
		}
		if(proyecto1==null){
			proyecto1=user.getEstudiante().getProyectos().get(0);
		}

		model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());

		if(proyecto1.getCantidad()>0) {
			Usuario usuario=new Usuario();
			model.addAttribute("usuario",usuario);
			model.addAttribute("estudiante","");
		}



		return "estudiante/registrar";
	}

	@PostMapping("/agregar-compañero")
	public String guardar(@RequestParam int semestre,@RequestParam double promedio,@Valid Usuario usuario,BindingResult result,SessionStatus status,Model model,Principal principal) {

		Proyecto p=this.proyecto.findByestudiante(principal.getName());

		if(p==null){
			Estudiante est=this.estudiante.findById(principal.getName()).orElse(null);
			p=this.proyecto.findById(est.getProyecto()).orElse(null);
			System.out.println("No funciono la couslta");
		}

		System.out.println(p.getTitulo());

		model.addAttribute("estudiante","");
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Registro de estudiante");
			return "estudiante/registrar";
		}


		if(this.user.getUsuarioByEmail(usuario.getEmail())!=null || this.user.getUsuarioById(usuario.getCodigo())!= null) {
			model.addAttribute("titulo", "Registro de estudiante");
			model.addAttribute("error", "error codigo o email ya utilizado");
			return "estudiante/registrar";
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
		es.setProyecto(p.getIdProyecto());
		p.setCantidad(p.getCantidad()-1);

		this.user.save(usuario);
		this.user.saveEstudent(es);
		this.proyecto.save(p);

		status.setComplete();
		return "redirect:/estudiante/info";
	}
	
	@GetMapping("/eliminar-documento/{id}")
	public String eliminarDocumento(@PathVariable int id,Principal principal){
		
		
		this.oploadDocu.delete(this.documento.findById(id).orElse(null).getUrl());
		this.documento.deleteById(id);
		Usuario user=this.user.getUsuarioById(principal.getName());
		if(user.getEstudiante().getProyectos().get(0).getDocumentos()==null || user.getEstudiante().getProyectos().get(0).getDocumentos().isEmpty()) {
			System.out.println("no le queda mas");
			return "redirect:/estudiante/add-documento";
		}
		System.out.println("si le queda mas");
		return "redirect:/estudiante/see-documento";
	}
	
	@GetMapping("/info")
	public String verInformacion(Principal principal,Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		model.addAttribute("estudiante", estudiante);
		model.addAttribute("personal", "");
		model.addAttribute("nombre", (estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		return "estudiante/info-estudiante";
	}

	@GetMapping("/editar-propuesta")
	public String editarPropuesta(Principal principal,Model model,RedirectAttributes flash) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		Proyecto proyecto1 = this.proyecto.findById(estudiante.getEstudiante().getProyecto()).orElse(null);

		boolean proyecto=proyecto1==null;
		if((estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()) && proyecto1==null ) {
			flash.addFlashAttribute("infop","");
			return "redirect:/estudiante/subir";
		}

		model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("infoe", "");

		if (proyecto) {
			proyecto1=estudiante.getEstudiante().getProyectos().get(0);
		}
		model.addAttribute("documento",proyecto1.getDocumento());
		model.addAttribute("estudiante",proyecto1.getEstudiante().getIdEstudiantes());
		model.addAttribute("propuesta",proyecto1);
		return "estudiante/editar-propuesta";



	}
	
	@PostMapping("/guardar-edicion")
	public String guardarEdicion(@Valid Proyecto proyecto,BindingResult result,@RequestParam int tipo,
			Model model,@RequestParam(name = "archivo") MultipartFile archivo,SessionStatus status,@RequestParam String estudiante) {

		System.out.println(proyecto.getIdProyecto());
		if(result.hasErrors()) {
			model.addAttribute("propuesta", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			result.getFieldErrors().forEach(fieldError -> {
				System.out.println(fieldError.getField()+": "+fieldError.getDefaultMessage());
			});
			return "estudiante/editar-propuesta";
		}
		
		TipoProyecto tipoProyecto=tipe.findById(tipo).orElse(null);
		
		if(tipo==-1 || tipoProyecto==null) {
			model.addAttribute("propuesta", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("tipo", "Tipo no seleccionado o no valido");
			System.out.println("erro en el tipo de proyecto");
			return "estudiante/editar-propuesta";
		}
		
		if (!archivo.isEmpty()) {
			String arch=archivo.getOriginalFilename();
			int index=arch.lastIndexOf(".");


		
		if(index>0) {
			arch=arch.substring(index+1);

		if(!arch.equalsIgnoreCase("docx") && !arch.equalsIgnoreCase("doc")) {
			model.addAttribute("propuesta", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("archivo", "El archivo debe ser un formato .DOCX o .DOC");
			System.out.println("error en el formato del archivo");
			return "estudiante/subirPropuesta";
		}


			String uniqueFile=null;
			try {
				this.upload.delete(proyecto.getDocumento());
				uniqueFile=this.upload.copy(archivo);
				proyecto.setDocumento(uniqueFile);
			} catch (IOException e) {
				model.addAttribute("propuesta", proyecto);
				model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
				model.addAttribute("archivo", "el archivo no se pudo subir");
				System.out.println("erro al subir el archivo");
				return "estudiante/subirPropuesta";

			}
			
		}
		}
		Estudiante est=this.estudiante.findById(estudiante).orElse(null);
		
		proyecto.setEstado(this.estado.findById(1).orElse(null));
		proyecto.setTipoProyecto(tipoProyecto);
		proyecto.setCorreccion(true);
		proyecto.setEstudiante(est);
		System.out.println("completo");
		this.proyecto.save(proyecto);
			status.setComplete();

		return "redirect:/estudiante/info-propuesta";
	}


	
	@PostMapping("/guardar")
	public String guardarPropuesta(Principal principal,@Valid Proyecto proyecto,BindingResult result,@RequestParam int tipo,
			Model model,@RequestParam String director,@RequestParam int primera,@RequestParam(name = "archivo") MultipartFile archivo,SessionStatus status) {

		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());

		if(result.hasErrors()) {
			System.out.println("Entro a la parte de la validacion");
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			return "estudiante/subirPropuesta";
		}

		if(primera!= 1){ proyecto.setCorreccion(true);}
		else proyecto.setCorreccion(false);


		TipoProyecto tipoProyecto=tipe.findById(tipo).orElse(null);
		
		if(tipo==-1 || tipoProyecto==null) {
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("tipo", "Tipo no seleccionado o no valido");
			return "estudiante/subirPropuesta";
		}
		
		if (archivo.isEmpty()) {
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("archivo", "Archivo no seleccionado");
			return "estudiante/subirPropuesta";
		}
		String arch=archivo.getOriginalFilename();
		int index=arch.lastIndexOf(".");
		
		if(index>0) {
			arch=arch.substring(index+1);
			
		}
		
		if(!arch.equalsIgnoreCase("docx") && !arch.equalsIgnoreCase("doc")) {
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("archivo", "El archivo debe ser un formato .DOCX o .DOC");
			return "estudiante/subirPropuesta";
		}

		if(director.isBlank()){
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("director", "El codigo del director no debe ser vacio");
			return "estudiante/subirPropuesta";
		}

		Tutor tutor =this.tutor.findById(director.trim()).orElse(null);

		if(tutor==null){
			model.addAttribute("proyecto", proyecto);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("director", "El tutor no se encuentra en la base de datos");
			return "estudiante/subirPropuesta";
		}

			proyecto.setEstado(this.estado.findById(1).orElse(null));
			proyecto.setTipoProyecto(tipoProyecto);
			proyecto.setEstudiante(estudiante.getEstudiante());
			proyecto.setCantidad(2);
			proyecto.setInicio(new Date());
			
			Calendar c=Calendar.getInstance();
			c.add(Calendar.MONTH,proyecto.getDuracion());
			
			proyecto.setFinalizacion(c.getTime());
			
			
			String uniqueFile=null;
			try {
				uniqueFile=this.upload.copy(archivo);
				proyecto.setDocumento(uniqueFile+"."+arch);
			} catch (IOException e) {
				model.addAttribute("proyecto", proyecto);
				model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
				model.addAttribute("archivo", "el archivo no se pudo subir");
				return "estudiante/subirPropuesta";
			}

			Dirige dirige =new Dirige();
			dirige.setProyecto(proyecto);
			dirige.setFecha(new Date());
			dirige.setTutor(tutor);

			this.proyecto.save(proyecto);
			this.dirigeDao.save(dirige);

			status.setComplete();
		
		
		return "redirect:/estudiante/info-propuesta";
	}
	
	@GetMapping("/info-propuesta")
	public String ver(Principal principal, Model model, RedirectAttributes flash) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		Proyecto proyecto=this.proyecto.findById(estudiante.getEstudiante().getProyecto()).orElse(null);


		if((estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()) &&  proyecto==null){
			flash.addFlashAttribute("infop", "");
			return "redirect:/estudiante/subir";
		}

		if(proyecto==null){
		 proyecto=estudiante.getEstudiante().getProyectos().get(0);
		}

		try{
			Tutor tut=this.dirigeDao.getByPropuesta(proyecto.getIdProyecto()).getTutor();
			model.addAttribute("tutor",tut);
		}catch (Exception e){
			model.addAttribute("tutor",null);
		}

		System.out.println("Entro al metodo");
		model.addAttribute("propuesta", proyecto);

		model.addAttribute("infop","");
		model.addAttribute("nombre", (estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("titulo", "Informacion de la propuesta");
		return "estudiante/info-propuesta";
	}
	
	 @RequestMapping("/download-document/{filename}")
	 @ResponseBody
	 public void show(@PathVariable String filename, HttpServletResponse response) {
		 
		 response.setContentType("application/pdf");
		 response.setHeader("Content-Transfer-Encoding", "quoted-printable");
		 
		 try {
			 BufferedOutputStream bos= new BufferedOutputStream(response.getOutputStream());
			 FileInputStream file =new FileInputStream(this.oploadDocu.getPath(filename).toString());
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
	
	 @Secured({"ROLE_EVALUADOR","ROLE_ADMINISTRADOR","ROLE_ESTUDIANTE"})
	 @RequestMapping("/download/{filename}")
	 @ResponseBody
	 public void shows(@PathVariable String filename, HttpServletResponse response) {
		 
			 response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		 
		 response.setHeader("Content-Disposition", "attachment: filename="+filename);
		 response.setHeader("Content-Transfer-Encoding", "binary");
		 
		 try {
		 	filename=filename.replace(".docx","");
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
	 
	 
	 @GetMapping("/calificadores")
	    public String verCalificadores(Model model,Principal principal, RedirectAttributes flash) {
	    
		 Usuario user=this.user.getUsuarioById(principal.getName());
	    Proyecto proyecto=this.proyecto.findById(user.getEstudiante().getProyecto()).orElse(null);
	    	
	    	if(proyecto==null ) { 
	    		proyecto=user.getEstudiante().getProyectos().get(0);
	    		}
	    	
	    	List<Usuario> calificadores=proyecto.getAsiganados().stream().map(e -> e.getEvaluador().getUsuario()).collect(Collectors.toList());
	    
	    	model.addAttribute("nombre",(user.getNombres()+" "+user.getApellidos()).toUpperCase());
	    	model.addAttribute("calificadores",calificadores);
	    	model.addAttribute("id", proyecto.getIdProyecto());
	    	
	    	
	    return "tutor/calificadores";
	    }
	 
	 @GetMapping("/{idProyecto}/calificaciones/{idTutor}")
	 public String verCalificacion(Model model, Principal principal,@PathVariable String idTutor,@PathVariable int idProyecto) {
		 Usuario user=this.user.getUsuarioById(principal.getName());
		 
		Calificado calificado= this.calificado.getActualByCalificadorAndProyecto(idTutor, idProyecto);
		 if(calificado==null) {
			 return "redirect:/estudiante/calificadores";
		 }
		 
		 model.addAttribute("calificado",calificado);
		 model.addAttribute("ver","");
		 model.addAttribute("titulo", "calificaciones");
		 model.addAttribute("nombre", (user.getNombres()+" "+user.getApellidos()).toUpperCase());
		 return "calificador/calificacion";
	 }
	
	
}
