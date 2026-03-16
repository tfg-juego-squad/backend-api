package org.example.backendapi.control;

import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Usuario;
import org.example.backendapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable(value = "id") String id){
        Optional<Usuario> usuario = usuarioDAO.findUsuarioById(id);
        if(usuario.isPresent()){
            return ResponseEntity.ok().body(usuario.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> buscarUsuariosPorNombre(@PathVariable(value = "nombre") String nombre){
        List<Usuario> listaUsuarios = usuarioDAO.findUsuarioByNombreUsuario(nombre);
        if(!listaUsuarios.isEmpty()){
            return ResponseEntity.ok().body(listaUsuarios);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> buscarUsuariosPorRol(@PathVariable(value = "rol") String rol){
        List<Usuario> listaUsuarios = usuarioDAO.findUsuarioByRol(rol);
        if(!listaUsuarios.isEmpty()){
            return ResponseEntity.ok().body(listaUsuarios);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Validated
    @PostMapping("/alta")
    public ResponseEntity<Usuario> guardarUsuario(@Validated @RequestBody Usuario usuario){
        Usuario guardado = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario nuevoUsuario,
                                                @PathVariable(value = "id") String id) {
        Optional<Usuario> usuario = usuarioDAO.findById(id);
        if (usuario.isPresent()) {
            usuario.get().setNombreUsuario(nuevoUsuario.getNombreUsuario());
            usuario.get().setRol(nuevoUsuario.getRol());
            usuario.get().setFechaCreacion(nuevoUsuario.getFechaCreacion());
            usuario.get().setHashContrasena(nuevoUsuario.getHashContrasena());
            usuarioDAO.save(usuario.get());
            return ResponseEntity.ok().body("Actualizado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarUsuario (@PathVariable(value = "id") String id) {
        Optional<Usuario> usuario = usuarioDAO.findUsuarioById(id);
        if(usuario.isPresent()) {
            usuarioDAO.deleteById(id);
            return ResponseEntity.ok().body("Borrado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
