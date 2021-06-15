package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;

import java.util.Date;
import java.util.List;

public interface ProyectoDao extends CrudRepository<Proyecto, Integer> {

    @Query("select p from Proyecto p where p.estudiante.idEstudiantes=?1")
    public Proyecto findByestudiante(String codigo);

    @Query("select p from Proyecto p join fetch p.estado e where e.nombre=?1")
    public List<Proyecto> findByEstado(String nombre);
    
    @Query("select p from Proyecto p join fetch p.estado e where e.nombre=?1 or e.nombre=?2")
    public List<Proyecto> findByTwoState(String string1,String string2);
    
    
    @Query("select p from Proyecto p  where p.inicio between ?1 and ?2")
    public List<Proyecto> entreFecha(Date inicio,Date fin);
}
