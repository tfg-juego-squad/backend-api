package org.example.backendapi.control;

import org.example.backendapi.model.dao.IInventarioDAO;
import org.example.backendapi.model.entities.Inventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tfg/inventarios")
public class InventarioControl {
    @Autowired
    IInventarioDAO inventarioDAO;

    @GetMapping
    public List<Inventario> buscarInventarios() {
        return (List<Inventario>) inventarioDAO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarInventarioPorId(@PathVariable(value = "id") String id){
        Optional<Inventario> inventario = inventarioDAO.findInventarioById(id);
        if(inventario.isPresent()){
            return ResponseEntity.ok().body(inventario.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre-objeto/{nombre}")
    public ResponseEntity<List<Inventario>> buscarInventariosPorNombreObjeto(@PathVariable(value = "nombre") String nombre){
        List<Inventario> listaInventarios = inventarioDAO.findInventarioByNombreObjeto(nombre);
        if(!listaInventarios.isEmpty()){
            return ResponseEntity.ok().body(listaInventarios);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Validated
    @PostMapping("/alta")
    public Inventario guardarInventario(@Validated @RequestBody Inventario inventario){
        return inventarioDAO.save(inventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(@RequestBody Inventario nuevoInventario,
                                              @PathVariable(value = "id") String id) {
        Optional<Inventario> inventario = inventarioDAO.findById(id);
        if (inventario.isPresent()) {
            inventario.get().setNombreObjeto(nuevoInventario.getNombreObjeto());
            inventario.get().setFechaAdquisicion(nuevoInventario.getFechaAdquisicion());
            inventarioDAO.save(inventario.get());
            return ResponseEntity.ok().body("Actualizado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrarInventario(@PathVariable(value = "id") String id) {
        Optional<Inventario> inventario = inventarioDAO.findInventarioById(id);
        if(inventario.isPresent()) {
            inventarioDAO.deleteById(id);
            return ResponseEntity.ok().body("Borrado");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
