package com.bolsadeideas.spring.horario.datajpa.app.service;
import javax.transaction.Transactional;

import com.bolsadeideas.spring.horario.datajpa.app.dao.UsuarioDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioDao usuarioDao;

    public void updateResetPasswordToken(String token, String codigo) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findById(codigo).orElse(null);
        if (usuario != null) {
            usuario.setTokenPassword(token);
            usuarioDao.save(usuario);
        } else {
            throw new UsernameNotFoundException("La consulta no encontro ningun usuario con el codigo: " + codigo);
        }
    }

    public Usuario getByResetPasswordToken(String token) {
        return usuarioDao.findByTokenPassword(token);
    }

    public void updatePassword(Usuario usuario, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);

        usuario.setTokenPassword(null);
        usuarioDao.save(usuario);
    }
}