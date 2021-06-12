package com.bolsadeideas.spring.horario.datajpa.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Calificado;

public interface CalificadoDao extends CrudRepository<Calificado, Integer> {

	@Query("select c from Calificado c join fetch c.evaluador e where e.idEvaluador=?1 and c.actual=true")
	public Calificado getActualByCalificador(String calificador);
	
	@Query("select c from Calificado c join fetch c.proyecto p where p.idProyecto=?1 and c.actual=true")
	public List<Calificado> getAllActualById(int idProyecto);
}
