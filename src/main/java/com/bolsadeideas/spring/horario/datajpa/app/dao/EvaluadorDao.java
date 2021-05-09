package com.bolsadeideas.spring.horario.datajpa.app.dao;

import com.bolsadeideas.spring.horario.datajpa.app.models.Evaluador;
import org.springframework.data.repository.CrudRepository;

public interface EvaluadorDao extends CrudRepository<Evaluador,String> {
}
