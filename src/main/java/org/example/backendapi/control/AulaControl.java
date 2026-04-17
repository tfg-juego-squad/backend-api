package org.example.backendapi.control;

import org.example.backendapi.model.dao.IAulaDAO;
import org.example.backendapi.model.dao.IUsuarioDAO;
import org.example.backendapi.model.entities.Aula;
import org.example.backendapi.model.entities.TipoRol;
import org.example.backendapi.model.entities.Usuario;
import org.example.backendapi.service.AulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tfg/aulas")
public class AulaControl {

    @Autowired
    private IAulaDAO aulaDAO;

    @Autowired
    private AulaService aulaService;

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Aula>> getAulasByProfesor(@PathVariable String profesorId) {
        List<Aula> aulas = aulaDAO.findAulasByProfesorId(profesorId);
        if (aulas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204: Petición correcta pero no hay aulas
        }
        return ResponseEntity.ok(aulas);
    }

    @GetMapping("/{aulaId}/alumnos")
    public ResponseEntity<List<Usuario>> getAlumnosByAula(@PathVariable String aulaId) {
        Optional<Aula> aulaOpt = aulaDAO.findAulaById(aulaId);

        if (aulaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aula aula = aulaOpt.get();

        List<Usuario> alumnos = aula.getAlumnos().stream()
                .filter(u -> u.getRol() == TipoRol.ROL_ESTUDIANTE)
                .collect(Collectors.toList());

        return ResponseEntity.ok(alumnos);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearAula(@RequestParam String nombreAula, @RequestParam String profesorId) {
        try {
            return ResponseEntity.ok(aulaService.crearAula(nombreAula, profesorId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{aulaId}/generar-alumnos")
    public ResponseEntity<?> generarAlumnos(@PathVariable String aulaId, @RequestParam int cantidad) {
        try {
            List<Map<String, String>> credenciales = aulaService.generarAlumnosParaAula(aulaId, cantidad);
            return ResponseEntity.ok(credenciales);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}