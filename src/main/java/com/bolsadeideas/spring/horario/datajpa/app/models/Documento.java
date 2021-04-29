package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


/**
 * The persistent class for the documento database table.
 * 
 */
@Entity
@Table(name="documento")
@NamedQuery(name="Documento.findAll", query="SELECT d FROM Documento d")
public class Documento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_documento")
	private int idDocumento;

	@NotBlank(message="El campo dela descripcion no puede ser vacio")
	private String descripcion;

	@NotBlank(message="El campo del nombre no puede ser vacio")
	private String nombre;

	private String url;

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="id_proyecto")
	private Proyecto proyecto;

	public Documento() {
	}

	public int getIdDocumento() {
		return this.idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

}