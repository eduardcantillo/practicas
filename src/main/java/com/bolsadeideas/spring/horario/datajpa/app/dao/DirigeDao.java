package com.bolsadeideas.spring.horario.datajpa.app.dao;

import com.bolsadeideas.spring.horario.datajpa.app.models.Dirige;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DirigeDao extends CrudRepository<Dirige,Integer> {
    @Query("select d from Dirige d where d.tutor.idTutor=?1")
    public List<Dirige> getByTutor(String idTutor);

   @Query("select d from Dirige d join fetch d.proyecto p where p.idProyecto=?1")
    public Dirige getByPropuesta (int idProyecto);
}
