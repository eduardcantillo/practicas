package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
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

	private double nota;

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