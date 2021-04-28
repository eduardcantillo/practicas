package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Estado;

public interface EstadoDao extends CrudRepository<Estado, Integer> {

}
