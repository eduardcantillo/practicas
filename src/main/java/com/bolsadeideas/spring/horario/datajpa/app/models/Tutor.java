package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tutor database table.
 * 
 */
@Entity
@Table(name="tutor")
@NamedQuery(name="Tutor.findAll", query="SELECT t FROM Tutor t")
public class Tutor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_tutor")
	private String idTutor;

	//bi-directional many-to-one association to Calificado
	@OneToMany(mappedBy="tutor")
	private List<Calificado> calificados;

	//bi-directional many-to-one association to Dirige
	@OneToMany(mappedBy="tutor")
	private List<Dirige> diriges;

	//bi-directional one-to-one association to Usuario
	@OneToOne
	@JoinColumn(name="id_tutor")
	private Usuario usuario;

	public Tutor() {
	}

	public String getIdTutor() {
		return this.idTutor;
	}

	public void setIdTutor(String idTutor) {
		this.idTutor = idTutor;
	}

	public List<Calificado> getCalificados() {
		return this.calificados;
	}

	public void setCalificados(List<Calificado> calificados) {
		this.calificados = calificados;
	}

	public Calificado addCalificado(Calificado calificado) {
		getCalificados().add(calificado);
		calificado.setTutor(this);

		return calificado;
	}

	public Calificado removeCalificado(Calificado calificado) {
		getCalificados().remove(calificado);
		calificado.setTutor(null);

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
		dirige.setTutor(this);

		return dirige;
	}

	public Dirige removeDirige(Dirige dirige) {
		getDiriges().remove(dirige);
		dirige.setTutor(null);

		return dirige;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}