package org.example.backendapi.control;

import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Usuario;
import org.example.backendapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tfg/usuarios")
public class UsuarioControl {
    @Autowired
    IUsuarioDAO usuarioDAO;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> buscarUsuarios() {
        return (List<Usuario>) usuarioDAO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable String id){
        Optional<Usuario> usuario = usuarioDAO.findUsuarioById(id);
        if(usuario.isPresent()){
            return ResponseEntity.ok().body(usuario.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> buscarUsuariosPorNombre(@PathVariable String nombre){
        List<Usuario> listaUsuarios = usuarioDAO.findUsuarioByNombreUsuario(nombre);
        if(!listaUsuarios.isEmpty()){
            return ResponseEntity.ok().body(listaUsuarios);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profesor/alta")
    public ResponseEntity<?> registrarProfesor(@Validated @RequestBody Usuario profesor){
        try {
            Usuario guardado = usuarioService.registrarProfesor(profesor);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/aulas/crear")
    public ResponseEntity<?> crearAula(@RequestParam String nombreAula, @RequestParam String profesorId) {
        try {
            return ResponseEntity.ok(usuarioService.crearAula(nombreAula, profesorId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. El Profesor genera los alumnos para su Aula
    @PostMapping("/aulas/{aulaId}/generar-alumnos")
    public ResponseEntity<?> generarAlumnos(@PathVariable String aulaId, @RequestParam int cantidad) {
        try {
            List<Map<String, String>> credenciales = usuarioService.generarAlumnosParaAula(aulaId, cantidad);
            return ResponseEntity.ok(credenciales); // Devuelve el JSON con los usuarios y contraseñas planas
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarUsuario (@PathVariable String id) {
        Optional<Usuario> usuario = usuarioDAO.findUsuarioById(id);
        if(usuario.isPresent()) {
            usuarioDAO.deleteById(id);
            return ResponseEntity.ok().body("Borrado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
