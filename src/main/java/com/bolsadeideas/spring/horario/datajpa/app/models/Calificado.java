package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import java.util.Date;


/**
 * The persistent class for the calificado database table.
 * 
 */
@Entity
@Table(name="calificado")
@NamedQuery(name="Calificado.findAll", query="SELECT c FROM Calificado c")
public class Calificado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_calificado")
	private int idCalificado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private boolean actual;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double titulo;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double justificacion;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double planteamiento;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double objetivos;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double alcances;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double marcoteorico;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double disenoMetodologico;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double preosupuestoYcronograma;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double referencias;
	
	// Evaluacion de la forma del anteproyecto 30%
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double cumplimineto;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private double redacion;
	
	@NotBlank(message = "Selecione un estado valido")
	private String estado;
	
	private String documento;
	

	private double nota;

	@NotBlank(message = "Debe dejar una observacion")
	private String observaciones;

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="id_proyecto")
	private Proyecto proyecto;

	//bi-directional many-to-one association to Tutor
	@ManyToOne
	@JoinColumn(name="id_evaluador")
	private Evaluador evaluador;

	public Calificado() {
	}
	
	
	
	public String getEstado() {
		return estado;
	}



	public String getDocumento() {
		return documento;
	}



	public void setDocumento(String documento) {
		this.documento = documento;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public double getJustificacion() {
		return justificacion;
	}



	public void setJustificacion(double justificacion) {
		this.justificacion = justificacion;
	}



	public void setDisenoMetodologico(double disenoMetodologico) {
		this.disenoMetodologico = disenoMetodologico;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public boolean isActual() {
		return actual;
	}



	public double getTitulo() {
		return titulo;
	}



	public double getPlanteamiento() {
		return planteamiento;
	}



	public double getObjetivos() {
		return objetivos;
	}



	public double getAlcances() {
		return alcances;
	}



	public double getMarcoteorico() {
		return marcoteorico;
	}



	public double getDisenoMetodologico() {
		return disenoMetodologico;
	}



	public double getPreosupuestoYcronograma() {
		return preosupuestoYcronograma;
	}



	public double getReferencias() {
		return referencias;
	}



	public double getCumplimineto() {
		return cumplimineto;
	}



	public double getRedacion() {
		return redacion;
	}



	public void setActual(boolean actual) {
		this.actual = actual;
	}



	public void setTitulo(double titulo) {
		this.titulo = titulo;
	}



	public void setPlanteamiento(double planteamiento) {
		this.planteamiento = planteamiento;
	}



	public void setObjetivos(double objetivos) {
		this.objetivos = objetivos;
	}



	public void setAlcances(double alcances) {
		this.alcances = alcances;
	}



	public void setMarcoteorico(double marcoteorico) {
		this.marcoteorico = marcoteorico;
	}



	public void setDiseñoMetodologico(double disenoMetodologico) {
		this.disenoMetodologico = disenoMetodologico;
	}



	public void setPreosupuestoYcronograma(double preosupuestoYcronograma) {
		this.preosupuestoYcronograma = preosupuestoYcronograma;
	}



	public void setReferencias(double referencias) {
		this.referencias = referencias;
	}



	public void setCumplimineto(double cumplimineto) {
		this.cumplimineto = cumplimineto;
	}



	public void setRedacion(double redacion) {
		this.redacion = redacion;
	}



	public void setEvaluador(Evaluador evaluador) {
		this.evaluador = evaluador;
	}



	public int getIdCalificado() {
		return this.idCalificado;
	}

	public void setIdCalificado(int idCalificado) {
		this.idCalificado = idCalificado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getNota() {
		return this.nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public Evaluador getEvaluador() {
		return this.evaluador;
	}

	public void setTutor(Evaluador evaluador) {
		this.evaluador = evaluador;
	}

}