package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the evaluador database table.
 * 
 */
@Entity
@Table(name="evaluador")
@NamedQuery(name="Evaluador.findAll", query="SELECT e FROM Evaluador e")
public class Evaluador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_evaluador")
	private String idEvaluador;

	//bi-directional many-to-one association to Asiganado
	@OneToMany(mappedBy="evaluador")
	private List<Asiganado> asiganados;

	//bi-directional one-to-one association to Usuario
	@OneToOne
	@JoinColumn(name="id_evaluador")
	private Usuario usuario;

	public Evaluador() {
	}

	public String getIdEvaluador() {
		return this.idEvaluador;
	}

	public void setIdEvaluador(String idEvaluador) {
		this.idEvaluador = idEvaluador;
	}

	public List<Asiganado> getAsiganados() {
		return this.asiganados;
	}

	public void setAsiganados(List<Asiganado> asiganados) {
		this.asiganados = asiganados;
	}

	public Asiganado addAsiganado(Asiganado asiganado) {
		getAsiganados().add(asiganado);
		asiganado.setEvaluador(this);

		return asiganado;
	}

	public Asiganado removeAsiganado(Asiganado asiganado) {
		getAsiganados().remove(asiganado);
		asiganado.setEvaluador(null);

		return asiganado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}