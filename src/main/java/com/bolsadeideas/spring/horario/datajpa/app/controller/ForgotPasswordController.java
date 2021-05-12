package com.bolsadeideas.spring.horario.datajpa.app.controller;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.bolsadeideas.spring.horario.datajpa.app.dao.UsuarioDao;
import com.bolsadeideas.spring.horario.datajpa.app.models.Usuario;
import com.bolsadeideas.spring.horario.datajpa.app.service.UsuarioService;
import com.bolsadeideas.spring.horario.datajpa.app.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioDao usuarioDao;


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("titulo","Cambiar contraseña");
        return "forgotPassword";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String codigo = request.getParameter("codigo");
        String token = RandomString.make(150);
        model.addAttribute("titulo","Cambiar contraseña");


        try {
            usuarioService.updateResetPasswordToken(token, codigo);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            Usuario u = usuarioDao.findById(codigo).orElse(null);
            sendEmail(u.getEmail(), resetPasswordLink,(u.getNombres()+" "+u.getApellidos()).toUpperCase());
            model.addAttribute("message", "Te hemos enviado un link al correo asociado al codigo para recuperar la contraseña. Por favor revisa.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", "No se encontro ningun usuario con el codigo digitado");
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error mientras se enviaba el email");
        }

        return "forgotPassword";
    }

    public void sendEmail(String recipientEmail, String link,String nombre)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("trabajosdegrados@ufps.edu.co", "Portal para los tranajos de grados");
        helper.setTo(recipientEmail);

        String subject = "Cambio de contraseña contraseña";

        String content = "<p>Hola, "+nombre+"</p>"
                + "<p>Tienes una solicitud para cambiar contraseña.</p>"
                + "<p>Click en el siguiente link para restaurarla:</p>"
                + "<p><a href=\"" + link + "\">Cambiar mi contraseña</a></p>"
                + "<br>"
                + "<p>Ignore este email si tu recuerdas tu contraseña , "
                + "o si no has realizado esta peticion.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Usuario usuario = usuarioService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("titulo","cambio de contraseña");
        if (usuario == null) {
            model.addAttribute("message", "Invalid Token");
            return "resetPassword";
        }

        return "resetPassword";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestParam("password") String pass, @RequestParam("passwordRepeat") String passRepeat,
                                       @RequestParam("token")String token,Model model, RedirectAttributes flash) {
        model.addAttribute("titulo","cambio de contraseña");
        if(!pass.equals(passRepeat)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Contraseñas no coinciden!");
            return "resetPassword";
        }

        Usuario usuario = usuarioService.getByResetPasswordToken(token);

        if (usuario == null) {
            model.addAttribute("message", "Invalid Token");
            return "resetPassword";
        } else {
            usuarioService.updatePassword(usuario, pass);

            flash.addAttribute("success", "Contraseña cambiada con exito!");
        }

        return "redirect:/login";
    }
}