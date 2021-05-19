package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;

import java.util.List;

public interface UsuarioDao extends CrudRepository<Usuario, String> {

	@Query("select c from Usuario c  where c.email=?1")
	public Usuario getByEmail(String email);
	public Usuario findByTokenPassword(String token);

	@Query("select u from Usuario u where u.habilitado=?1 and u.rol='ROLE_TUTOR'")
	public List<Usuario> getTutoreHabilitados(byte habilitado);
}
