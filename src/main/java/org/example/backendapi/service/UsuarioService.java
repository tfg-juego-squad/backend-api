package org.example.backendapi.service;

import jakarta.transaction.Transactional;
import org.example.backendapi.model.dao.IAulaDAO;
import org.example.backendapi.model.dao.IRolDAO;
import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Aula;
import org.example.backendapi.model.entities.Rol;
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

    @Autowired
    private IRolDAO rolDAO;

    public Usuario registrarProfesor(Usuario profesor) {
        profesor.setHashContrasena(hashPassword(profesor.getHashContrasena()));
        profesor.setFechaCreacion(Instant.now());

        Optional<Rol> rolProfe = rolDAO.findByNombre("ROL_PROFESOR");
        if(rolProfe.isPresent()) {
            profesor.setRoles(Set.of(rolProfe.get()));
        } else {
            throw new RuntimeException("Error: El rol ROL_PROFESOR no existe en la base de datos.");
        }

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

        Rol rolEstudiante = rolDAO.findByNombre("ROL_ESTUDIANTE").orElseThrow(() -> new RuntimeException("Error: El ROLE_ESTUDIANTE no existe en la base de datos"));

        // Aquí guardaremos los usuarios y contraseñas PLANAS para devolvérselas al profesor
        List<Map<String, String>> credencialesGeneradas = new ArrayList<>();

        for (int i = 1; i <= cantidad; i++) {
            Usuario alumno = new Usuario();

            String nombreUsuario = generarNombreUsuario(aula.getNombre(), i);
            String passwordPlana = generarPasswordAleatoria(6);

            alumno.setNombreUsuario(nombreUsuario);
            alumno.setHashContrasena(hashPassword(passwordPlana));
            alumno.setFechaCreacion(Instant.now());
            alumno.setAula(aula);
            alumno.setRoles(Set.of(rolEstudiante));

            // 3.3 Guardar en base de datos
            usuarioDAO.save(alumno);

            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("usuario", nombreUsuario);
            credenciales.put("password", passwordPlana);
            credencialesGeneradas.add(credenciales);
        }

        return credencialesGeneradas; // Devolvemos la lista de credenciales generadas
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

    private String generarPasswordAleatoria(int length) {
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            pass.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        }
        return pass.toString();
    }
}
