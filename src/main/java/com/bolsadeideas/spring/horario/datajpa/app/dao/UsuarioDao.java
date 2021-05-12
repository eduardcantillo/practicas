package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;

public interface UsuarioDao extends CrudRepository<Usuario, String> {

	@Query("select c from Usuario c  where c.email=?1")
	public Usuario getByEmail(String email);

	public Usuario findByTokenPassword(String token);
}
