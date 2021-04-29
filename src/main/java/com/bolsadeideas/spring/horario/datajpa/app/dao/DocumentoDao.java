package com.bolsadeideas.spring.horario.datajpa.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.spring.horario.datajpa.app.models.Documento;

public interface DocumentoDao extends CrudRepository<Documento, Integer> {

}
