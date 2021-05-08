package com.bolsadeideas.spring.horario.datajpa.app.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bolsadeideas.spring.horario.datajpa.app.dao.*;
import com.bolsadeideas.spring.horario.datajpa.app.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
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
@SessionAttributes(value = {"estudiante","propuesta","usuario","documento"})
public class EstudianteController {
	
	@Autowired
	private IUsuarioService user;
	@Autowired
	private TipoProyectoDao tipe;

	
	@Autowired
	private EstadoDao estado;
	
	@Autowired
	private IUploadService upload;
	
	@Autowired
	private ProyectoDao proyecto;

	@Autowired
	@Qualifier("documentos")
	private UploadFileDocum oploadDocu;

	@Autowired
	private DirigeDao dirigeDao;
	@Autowired 
	private DocumentoDao documento;
	
	
	
	@GetMapping("/subir")
	public String index(Principal principal,Model model) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		
		if(estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()) {
			System.out.println(estudiante);
			Proyecto propuesta =new Proyecto();
			model.addAttribute("titulo", "subir propuesta");
			model.addAttribute("estudiante", estudiante);
			model.addAttribute("propuesta", propuesta);
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
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("documentos", estudiante.getEstudiante().getProyectos().get(0).getDocumentos());
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
		
		documento.setProyecto(estudiante.getEstudiante().getProyectos().get(0));
		this.documento.save(documento);
		
		
		
		return "redirect:/estudiante/see-documento";
	}
	
	@PostMapping("/guardar-estudiante")
	public String guardar(@RequestParam int semestre,@RequestParam double promedio,@Valid Usuario usuario,BindingResult result,SessionStatus status,Model model) {
		
		usuario.setRol("ROLE_ESTUDIANTE");
		System.out.println("Entro al metodo");
		if(result.hasErrors()) {
			model.addAttribute("promedio", promedio);
			model.addAttribute("semestre", semestre);
			model.addAttribute("titulo", "Registro de estudiante");
			return "estudiante/edit";
		}
		
	
		System.out.println(semestre+ " "+ promedio);
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
		if(estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()) {
			flash.addFlashAttribute("infop","");
			return "redirect:/estudiante/subir";
		}
		model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
		model.addAttribute("nombre",(estudiante.getNombres()+" "+estudiante.getApellidos()).toUpperCase());
		model.addAttribute("infoe", "");
		model.addAttribute("propuesta",estudiante.getEstudiante().getProyectos().get(0));
		return "estudiante/editar-propuesta";
	}
	
	@PostMapping("/guardar-edicion")
	public String guardarEdicion(Principal principal,@Valid Proyecto propuesta,BindingResult result,@RequestParam int tipo,
			Model model,@RequestParam(name = "archivo") MultipartFile archivo,SessionStatus status) {
		
		
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		
		
		if(result.hasErrors()) {
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			
			return "estudiante/editar-propuesta";
		}
		
		TipoProyecto tipoProyecto=tipe.findById(tipo).orElse(null);
		
		if(tipo==-1 || tipoProyecto==null) {
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("tipo", "Tipo no seleccionado o no valido");
			return "estudiante/editar-propuesta";
		}
		
		if (!archivo.isEmpty()) {
			
		}
		String arch=archivo.getOriginalFilename();
		int index=arch.lastIndexOf(".");
		
		if(index>0) {
			arch=arch.substring(index+1);
			
		
		
		if(!arch.equalsIgnoreCase("docx") && !arch.equalsIgnoreCase("doc")) {
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("archivo", "El archivo debe ser un formato .DOCX o .DOC");
			return "estudiante/subirPropuesta";
		}
			
			
			
			
			String uniqueFile=null;
			try {
				this.upload.delete(propuesta.getDocumento());
				uniqueFile=this.upload.copy(archivo);
				propuesta.setDocumento(uniqueFile);
			} catch (IOException e) {
				model.addAttribute("propuesta", propuesta);
				model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
				model.addAttribute("archivo", "el archivo no se pudo subir");
				return "estudiante/subirPropuesta";
			}
			
		}
		
		propuesta.setEstado(this.estado.findById(1).orElse(null));
		propuesta.setTipoProyecto(tipoProyecto);
		propuesta.setEstudiante(estudiante.getEstudiante());
			this.proyecto.save(propuesta);
			status.setComplete();
		
		
		return "redirect:/estudiante/info-propuesta";
	}

	
	@PostMapping("/guardar")
	public String guardarPropuesta(Principal principal,@Valid Proyecto propuesta,BindingResult result,@RequestParam int tipo,
			Model model,@RequestParam(name = "archivo") MultipartFile archivo,SessionStatus status) {
		
		
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		
		
		if(result.hasErrors()) {
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			
			return "estudiante/subirPropuesta";
		}
		
		TipoProyecto tipoProyecto=tipe.findById(tipo).orElse(null);
		
		if(tipo==-1 || tipoProyecto==null) {
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("tipo", "Tipo no seleccionado o no valido");
			return "estudiante/subirPropuesta";
		}
		
		if (archivo.isEmpty()) {
			model.addAttribute("propuesta", propuesta);
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
			model.addAttribute("propuesta", propuesta);
			model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
			model.addAttribute("archivo", "El archivo debe ser un formato .DOCX o .DOC");
			return "estudiante/subirPropuesta";
		}
		
			propuesta.setEstado(this.estado.findById(1).orElse(null));
			propuesta.setTipoProyecto(tipoProyecto);
			propuesta.setEstudiante(estudiante.getEstudiante());
			
			
			
			
			String uniqueFile=null;
			try {
				uniqueFile=this.upload.copy(archivo);
				propuesta.setDocumento(uniqueFile+"."+arch);
			} catch (IOException e) {
				model.addAttribute("propuesta", propuesta);
				model.addAttribute("tipes", (List<TipoProyecto>)tipe.findAll());
				model.addAttribute("archivo", "el archivo no se pudo subir");
				return "estudiante/subirPropuesta";
			}
			this.proyecto.save(propuesta);
			status.setComplete();
		
		
		return "redirect:/estudiante/info-propuesta";
	}
	
	@GetMapping("/info-propuesta")
	public String ver(Principal principal,Model model,RedirectAttributes flash) {
		Usuario estudiante=this.user.getUsuarioById(principal.getName());
		if(estudiante.getEstudiante().getProyectos()==null || estudiante.getEstudiante().getProyectos().isEmpty()){
			flash.addFlashAttribute("infop", "");
			return "redirect:/estudiante/subir";
		}
		Proyecto pro=estudiante.getEstudiante().getProyectos().get(0);
		System.out.println("Entro al metodo");

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
	
	 @RequestMapping("/download/{filename}")
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
