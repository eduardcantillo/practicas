package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_proyecto database table.
 * 
 */
@Entity
@Table(name="tipo_proyecto")
@NamedQuery(name="TipoProyecto.findAll", query="SELECT t FROM TipoProyecto t")
public class TipoProyecto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_tipo_proyecto")
	private int idTipoProyecto;

	private String descripcion;

	private byte documentos;

	private String name;

	//bi-directional many-to-one association to Proyecto
	@OneToMany(mappedBy="tipoProyecto")
	private List<Proyecto> proyectos;

	public TipoProyecto() {
	}

	public int getIdTipoProyecto() {
		return this.idTipoProyecto;
	}

	public void setIdTipoProyecto(int idTipoProyecto) {
		this.idTipoProyecto = idTipoProyecto;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(byte documentos) {
		this.documentos = documentos;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public Proyecto addProyecto(Proyecto proyecto) {
		getProyectos().add(proyecto);
		proyecto.setTipoProyecto(this);

		return proyecto;
	}

	public Proyecto removeProyecto(Proyecto proyecto) {
		getProyectos().remove(proyecto);
		proyecto.setTipoProyecto(null);

		return proyecto;
	}

}