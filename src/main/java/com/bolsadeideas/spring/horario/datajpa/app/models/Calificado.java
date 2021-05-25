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
	private int titulo;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int justificacion;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int planteamiento;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int objetivos;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int alcances;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int marcoteorico;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int disenoMetodologico;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int preosupuestoYcronograma;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int referencias;
	
	// Evaluacion de la forma del anteproyecto 30%
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int cumplimineto;
	
	@Min(value = 1,message = "La nota debe estar entre 1 y 5")
	@Max(value=5,message = "La nota debe estar entre 1 Y 5")
	private int redacion;
	
	
	

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
	
	
	
	public int getJustificacion() {
		return justificacion;
	}



	public void setJustificacion(int justificacion) {
		this.justificacion = justificacion;
	}



	public void setDisenoMetodologico(int disenoMetodologico) {
		this.disenoMetodologico = disenoMetodologico;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public boolean isActual() {
		return actual;
	}



	public int getTitulo() {
		return titulo;
	}



	public int getPlanteamiento() {
		return planteamiento;
	}



	public int getObjetivos() {
		return objetivos;
	}



	public int getAlcances() {
		return alcances;
	}



	public int getMarcoteorico() {
		return marcoteorico;
	}



	public int getDisenoMetodologico() {
		return disenoMetodologico;
	}



	public int getPreosupuestoYcronograma() {
		return preosupuestoYcronograma;
	}



	public int getReferencias() {
		return referencias;
	}



	public int getCumplimineto() {
		return cumplimineto;
	}



	public int getRedacion() {
		return redacion;
	}



	public void setActual(boolean actual) {
		this.actual = actual;
	}



	public void setTitulo(int titulo) {
		this.titulo = titulo;
	}



	public void setPlanteamiento(int planteamiento) {
		this.planteamiento = planteamiento;
	}



	public void setObjetivos(int objetivos) {
		this.objetivos = objetivos;
	}



	public void setAlcances(int alcances) {
		this.alcances = alcances;
	}



	public void setMarcoteorico(int marcoteorico) {
		this.marcoteorico = marcoteorico;
	}



	public void setDiseñoMetodologico(int disenoMetodologico) {
		this.disenoMetodologico = disenoMetodologico;
	}



	public void setPreosupuestoYcronograma(int preosupuestoYcronograma) {
		this.preosupuestoYcronograma = preosupuestoYcronograma;
	}



	public void setReferencias(int referencias) {
		this.referencias = referencias;
	}



	public void setCumplimineto(int cumplimineto) {
		this.cumplimineto = cumplimineto;
	}



	public void setRedacion(int redacion) {
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