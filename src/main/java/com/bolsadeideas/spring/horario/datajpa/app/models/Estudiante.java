package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estudiantes database table.
 * 
 */
@Entity
@Table(name="estudiantes")
@NamedQuery(name="Estudiante.findAll", query="SELECT e FROM Estudiante e")
public class Estudiante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_estudiantes")
	private String idEstudiantes;

	private double promedio;

	private String semestre;

	//bi-directional one-to-one association to Usuario
	@OneToOne
	@JoinColumn(name="id_estudiantes")
	private Usuario usuario;

	//bi-directional many-to-one association to Proyecto
	@OneToMany(mappedBy="estudiante")
	private List<Proyecto> proyectos;

	public Estudiante() {
	}

	public String getIdEstudiantes() {
		return this.idEstudiantes;
	}

	public void setIdEstudiantes(String idEstudiantes) {
		this.idEstudiantes = idEstudiantes;
	}

	public double getPromedio() {
		return this.promedio;
	}

	public void setPromedio(double promedio) {
		this.promedio = promedio;
	}

	public String getSemestre() {
		return this.semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public Proyecto addProyecto(Proyecto proyecto) {
		getProyectos().add(proyecto);
		proyecto.setEstudiante(this);

		return proyecto;
	}

	public Proyecto removeProyecto(Proyecto proyecto) {
		getProyectos().remove(proyecto);
		proyecto.setEstudiante(null);

		return proyecto;
	}

}