package com.bolsadeideas.spring.horario.datajpa.app.service;

import com.bolsadeideas.spring.horario.datajpa.app.models.Estudiante;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;

public interface IUsuarioService {
	
	public Usuario save(Usuario user);
	public void update(Usuario user);
	public void delete(Usuario user);
	public void saveEstudent(Estudiante es);
	public Usuario getUsuarioByEmail(String email);
	public Usuario getUsuarioById(String id);
}
