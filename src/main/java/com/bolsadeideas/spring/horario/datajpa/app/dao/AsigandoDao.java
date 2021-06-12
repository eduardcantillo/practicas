package com.bolsadeideas.spring.horario.datajpa.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Asiganado;

public interface AsigandoDao extends CrudRepository<Asiganado, Integer>{

	@Query("select a from Asiganado a join fetch a.evaluador e where e.idEvaluador=?1 and a.calificable=?2")
	public List<Asiganado> getByestadoAndEvaludor(String id,boolean calificable);
	
	@Query("select a from Asiganado a join fetch a.evaluador e where e.idEvaluador=?1 and a.calificable=?2 and a.proyecto.idProyecto=?3")
	public Asiganado getByEstadoAndEvaluadorAndIdProyecto(String id,boolean calificable, int idProyecto);
	
	@Query("select a from Asiganado a join fetch a.proyecto p where p.idProyecto=?1")
	public List<Asiganado> getByIdProyecto(int idProyecto);
}
