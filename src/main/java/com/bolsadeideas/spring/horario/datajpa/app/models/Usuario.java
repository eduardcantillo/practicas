package com.bolsadeideas.spring.horario.datajpa.app.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;




/**
 * The persistent class for the usuarios database table.
 * 
 */
@Entity
@Table(name="usuarios")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Pattern(regexp = "[0-1]{1}[15]{2}[0-9]{4}", message = "Debe ser un codigo valido")
	private String codigo;

	@NotBlank(message = "El campo apellido no debe no ser vacio")
	private String apellidos;
	
	@NotBlank(message = "El campo direccionno debe no ser vacio")
	private String direccion;
	
	@Email(message = "EL campo debe ser tipo email")
	@NotBlank(message = "El campo Email debe no ser vacio")
	private String email;

	private byte habilitado;

	@NotBlank(message = "El campo nombre debe no ser vacio")
	private String nombres;
	
	@NotBlank(message = "El campo password no debe ser vacio")
	private String password;

	private String rol;

	@NotBlank(message = "el campo telefono no debe ser vacio")
	private String telefono;

	@Column(name = "token_password")
	private String tokenPassword;

	//bi-directional one-to-one association to Estudiante
	@OneToOne(mappedBy="usuario")
	private Estudiante estudiante;

	//bi-directional one-to-one association to Evaluador
	@OneToOne(mappedBy="usuario")
	private Evaluador evaluador;

	//bi-directional one-to-one association to Tutor
	@OneToOne(mappedBy="usuario")
	private Tutor tutor;

	public Usuario() {
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getHabilitado() {
		return this.habilitado;
	}

	public void setHabilitado(byte habilitado) {
		this.habilitado = habilitado;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Estudiante getEstudiante() {
		return this.estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public Evaluador getEvaluador() {
		return this.evaluador;
	}

	public void setEvaluador(Evaluador evaluador) {
		this.evaluador = evaluador;
	}

	public Tutor getTutor() {
		return this.tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

	public String getTokenPassword(){ return this.tokenPassword;}

	public void setTokenPassword(String tokenPassword){ this.tokenPassword=tokenPassword; }

}