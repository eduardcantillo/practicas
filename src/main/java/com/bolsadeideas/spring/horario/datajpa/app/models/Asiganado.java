package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the asiganado database table.
 * 
 */
@Entity
@Table(name="asiganado")
@NamedQuery(name="Asiganado.findAll", query="SELECT a FROM Asiganado a")
public class Asiganado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_asiganado")
	private int idAsiganado;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private boolean calificable;

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="id_proyecto")
	private Proyecto proyecto;

	//bi-directional many-to-one association to Evaluador
	@ManyToOne
	@JoinColumn(name="id_evaluador")
	private Evaluador evaluador;

	public Asiganado() {
	}

	public int getIdAsiganado() {
		return this.idAsiganado;
	}

	public void setIdAsiganado(int idAsiganado) {
		this.idAsiganado = idAsiganado;
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

	public Evaluador getEvaluador() {
		return this.evaluador;
	}

	public void setEvaluador(Evaluador evaluador) {
		this.evaluador = evaluador;
	}

	public boolean isCalificable() {
		return calificable;
	}

	public void setCalificable(boolean calificable) {
		this.calificable = calificable;
	}
	
	

}