package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the dirige database table.
 * 
 */
@Entity
@Table(name="dirige")
@NamedQuery(name="Dirige.findAll", query="SELECT d FROM Dirige d")
public class Dirige implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_dirige")
	private int idDirige;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="id_proyecto")
	private Proyecto proyecto;

	//bi-directional many-to-one association to Tutor
	@ManyToOne
	@JoinColumn(name="id_tutor")
	private Tutor tutor;

	public Dirige() {
	}

	public int getIdDirige() {
		return this.idDirige;
	}

	public void setIdDirige(int idDirige) {
		this.idDirige = idDirige;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public Tutor getTutor() {
		return this.tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

}