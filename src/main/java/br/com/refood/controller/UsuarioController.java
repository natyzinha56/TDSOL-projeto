package br.com.refood.controller;
import br.com.refood.model.Usuario;
import br.com.refood.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/usuarios") @CrossOrigin
public class UsuarioController {
 private final UsuarioRepository repo;
 public UsuarioController(UsuarioRepository repo){this.repo=repo;}
 @GetMapping public List<Usuario> listar(){return repo.findAll();}
 @GetMapping("/{id}") public Usuario buscar(@PathVariable Long id){return repo.findById(id).orElseThrow();}
 @PostMapping public ResponseEntity<?> criar(@Valid @RequestBody Usuario u){
  if(repo.existsByEmail(u.getEmail())) return ResponseEntity.badRequest().body("Este e-mail já está cadastrado.");
  return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(u));
 }
 @DeleteMapping("/{id}") public void excluir(@PathVariable Long id){repo.deleteById(id);}
}
