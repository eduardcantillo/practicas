package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Estudiante;

public interface IEstudianteDao extends CrudRepository<Estudiante, String> {

}
