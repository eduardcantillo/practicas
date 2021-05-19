package com.bolsadeideas.spring.horario.datajpa.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.spring.horario.datajpa.app.dao.IEstudianteDao;
import com.bolsadeideas.spring.horario.datajpa.app.dao.UsuarioDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Estudiante;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;

import java.util.List;

@Service
public class UsuarioServiceImple implements IUsuarioService {

	@Autowired
	private UsuarioDao user;
	
	@Autowired
	private IEstudianteDao estudiante;
	
	@Override
	@Transactional
	public Usuario save(Usuario user) {
		return this.user.save(user);
	}
	@Transactional
	@Override
	public void update(Usuario user) {
		this.user.save(user);
		
	}

	@Override
	public void delete(Usuario user) {
		this.user.delete(user);
		
	}

	@Override
	public void saveEstudent(Estudiante es) {
	this.estudiante.save(es);
		
	}
	@Override
	public Usuario getUsuarioByEmail(String email) {
		return this.user.getByEmail(email);
	}

	@Override
	public List<Usuario> getTutores(byte habilitado) {
		return this.user.getTutoreHabilitados(habilitado);
	}

	@Override
	public Usuario getUsuarioById(String id) {
		return this.user.findById(id).orElse(null);
	}


}
