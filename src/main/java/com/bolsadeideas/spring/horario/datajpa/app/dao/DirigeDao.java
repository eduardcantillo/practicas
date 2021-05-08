package com.bolsadeideas.spring.horario.datajpa.app.dao;

import com.bolsadeideas.spring.horario.datajpa.app.models.Dirige;
import org.springframework.data.repository.CrudRepository;

public interface DirigeDao extends CrudRepository<Dirige,Integer> {
}
