package com.bolsadeideas.spring.horario.datajpa.app.dao;

import com.bolsadeideas.spring.horario.datajpa.app.models.Dirige;
import com.bolsadeideas.spring.horario.datajpa.app.models.Tutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TutorDao extends CrudRepository<Tutor,String> {

}
