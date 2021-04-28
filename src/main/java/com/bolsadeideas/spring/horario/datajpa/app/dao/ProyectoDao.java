package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Proyecto;

public interface ProyectoDao extends CrudRepository<Proyecto, Integer> {

}
