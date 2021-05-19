package com.bolsadeideas.spring.horario.datajpa.app.dao;

import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;

import java.util.List;

public interface ProyectoDao extends CrudRepository<Proyecto, Integer> {

    @Query("select p from Proyecto p where p.estudiante.idEstudiantes=?1")
    public Proyecto findByestudiante(String codigo);

    @Query("select p from Proyecto p where p.estado.nombre=?1")
    public List<Proyecto> findByEstado(String nombre);
}
