package com.bolsadeideas.spring.horario.datajpa.app.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the proyecto database table.
 * 
 */
@Entity
@Table(name="proyecto")
@NamedQuery(name="Proyecto.findAll", query="SELECT p FROM Proyecto p")
public class Proyecto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_proyecto")
	private int idProyecto;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date inicio;

	@NotBlank(message = "Debe dar una descripcion para el proyecto")
	private String decripcion;

	private String documento;

	private int cantidad;

	@NotBlank(message = "El titulo no puede estar en blanco")
	private String titulo;

	@Min(value = 6,message = "la cantidad minima de meses debe ser 6")
	private int duracion;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date finalizacion;

	private boolean correccion;
	//bi-directional many-to-one association to Asiganado
	@OneToMany(mappedBy="proyecto")
	private List<Asiganado> asiganados;

	//bi-directional many-to-one association to Calificado
	@OneToMany(mappedBy="proyecto",cascade = CascadeType.ALL)
	private List<Calificado> calificados;

	//bi-directional many-to-one association to Dirige
	@OneToMany(mappedBy="proyecto")
	private List<Dirige> diriges;

	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="proyecto")
	private List<Documento> documentos;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="id_estado")
	private Estado estado;

	//bi-directional many-to-one association to Estudiante
	@ManyToOne
	@JoinColumn(name="id_estudiante")
	private Estudiante estudiante;

	//bi-directional many-to-one association to TipoProyecto
	@ManyToOne
	@JoinColumn(name="id_tipo_proyecto")
	private TipoProyecto tipoProyecto;

	public Proyecto() {
	}

	@PrePersist
	private void prePersist(){
		this.inicio=new Date();
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFinalizacion() {
		return finalizacion;
	}

	public void setFinalizacion(Date finalizacion) {
		this.finalizacion = finalizacion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public boolean isCorreccion() {
		return correccion;
	}

	public void setCorreccion(boolean correccion) {
		this.correccion = correccion;
	}

	public int getIdProyecto() {
		return this.idProyecto;
	}

	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getDecripcion() {
		return this.decripcion;
	}

	public void setDecripcion(String decripcion) {
		this.decripcion = decripcion;
	}

	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Asiganado> getAsiganados() {
		return this.asiganados;
	}

	public void setAsiganados(List<Asiganado> asiganados) {
		this.asiganados = asiganados;
	}

	public Asiganado addAsiganado(Asiganado asiganado) {
		getAsiganados().add(asiganado);
		asiganado.setProyecto(this);

		return asiganado;
	}

	public Asiganado removeAsiganado(Asiganado asiganado) {
		getAsiganados().remove(asiganado);
		asiganado.setProyecto(null);

		return asiganado;
	}

	public List<Calificado> getCalificados() {
		return this.calificados;
	}

	public void setCalificados(List<Calificado> calificados) {
		this.calificados = calificados;
	}

	public Calificado addCalificado(Calificado calificado) {
		getCalificados().add(calificado);
		calificado.setProyecto(this);

		return calificado;
	}

	public Calificado removeCalificado(Calificado calificado) {
		getCalificados().remove(calificado);
		calificado.setProyecto(null);

		return calificado;
	}

	public List<Dirige> getDiriges() {
		return this.diriges;
	}

	public void setDiriges(List<Dirige> diriges) {
		this.diriges = diriges;
	}

	public Dirige addDirige(Dirige dirige) {
		getDiriges().add(dirige);
		dirige.setProyecto(this);

		return dirige;
	}

	public Dirige removeDirige(Dirige dirige) {
		getDiriges().remove(dirige);
		dirige.setProyecto(null);

		return dirige;
	}

	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public Documento addDocumento(Documento documento) {
		getDocumentos().add(documento);
		documento.setProyecto(this);

		return documento;
	}

	public Documento removeDocumento(Documento documento) {
		getDocumentos().remove(documento);
		documento.setProyecto(null);

		return documento;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Estudiante getEstudiante() {
		return this.estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public TipoProyecto getTipoProyecto() {
		return this.tipoProyecto;
	}

	public void setTipoProyecto(TipoProyecto tipoProyecto) {
		this.tipoProyecto = tipoProyecto;
	}

}