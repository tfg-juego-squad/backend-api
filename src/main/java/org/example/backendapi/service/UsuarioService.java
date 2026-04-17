package org.example.backendapi.service;

import jakarta.transaction.Transactional;
import org.example.backendapi.model.dao.IAulaDAO;
import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Aula;
import org.example.backendapi.model.entities.TipoRol;
import org.example.backendapi.model.entities.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class UsuarioService {
    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IAulaDAO aulaDAO;

    public Usuario registrarProfesor(Usuario profesor) {
        profesor.setHashContrasena(hashPassword(profesor.getHashContrasena()));
        profesor.setFechaCreacion(Instant.now());

        profesor.setRol(TipoRol.ROL_PROFESOR);

        return usuarioDAO.save(profesor);
    }

    public Aula crearAula(String nombreAula, String profesorId) {
        Usuario profesor = usuarioDAO.findUsuarioById(profesorId).orElseThrow(() -> new RuntimeException("Error: Profesor no encontrado"));

        Aula aula = new Aula();
        aula.setNombre(nombreAula);
        aula.setProfesor(profesor);

        aula.setCodigoInvitacion(UUID.randomUUID().toString().substring(0, 5).toUpperCase());

        return aulaDAO.save(aula);
    }

    @Transactional
    public List<Map<String, String>> generarAlumnosParaAula(String aulaId, int cantidad) {
        Aula aula = aulaDAO.findAulaById(aulaId).orElseThrow(() -> new RuntimeException("Aula no encontrada"));

        List<Map<String, String>> credencialesGeneradas = new ArrayList<>();

        for (int i = 1; i <= cantidad; i++) {
            Usuario alumno = new Usuario();

            String nombreUsuario = generarNombreUsuario(aula.getNombre(), i);
            String passwordPlana = generarPasswordAleatoria();

            alumno.setNombreUsuario(nombreUsuario);
            alumno.setHashContrasena(hashPassword(passwordPlana));
            alumno.setFechaCreacion(Instant.now());
            alumno.setAula(aula);
            alumno.setRol(TipoRol.ROL_ESTUDIANTE);

            usuarioDAO.save(alumno);

            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("usuario", nombreUsuario);
            credenciales.put("password", passwordPlana);
            credencialesGeneradas.add(credenciales);
        }

        return credencialesGeneradas;
    }

    private String hashPassword(String password) {
        int logRounds = 12;
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(password, salt);
    }

    private String generarNombreUsuario(String nombreAula, int numero) {
        String base = nombreAula.replaceAll("\\s+", "").toLowerCase();
        String sufijoUnico = UUID.randomUUID().toString().substring(0, 5);
        return base + "_alumno" + numero + "_" + sufijoUnico;
    }

    private String generarPasswordAleatoria() {
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            pass.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        }
        return pass.toString();
    }

    public Usuario hacerLogin(String nombreUsuario, String passwordPlana) {
        List<Usuario> usuarios = usuarioDAO.findUsuarioByNombreUsuario(nombreUsuario);

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Error: Usuario no encontrado");
        }

        Usuario usuario = usuarios.get(0);

        if (BCrypt.checkpw(passwordPlana, usuario.getHashContrasena())) {
            return usuario;
        } else {
            throw new RuntimeException("Error: Contraseña incorrecta");
        }
    }
}
