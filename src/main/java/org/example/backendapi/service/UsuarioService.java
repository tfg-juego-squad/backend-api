package org.example.backendapi.service;

import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UsuarioService {
    @Autowired
    private IUsuarioDAO usuarioDAO;

    public Usuario registrarUsuario(Usuario usuario) {
        String passwordEncriptada = hashPassword(usuario.getHashContrasena());
        usuario.setHashContrasena(passwordEncriptada);

        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(Instant.now());
        }

        return usuarioDAO.save(usuario);
    }

    public static String hashPassword(String password) {
        // Define a cost factor (work factor). Default is 10.
        int logRounds = 12;  // Increasing this value makes it more secure, but slower

        // Generate the salt
        String salt = BCrypt.gensalt(logRounds);

        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }
}
