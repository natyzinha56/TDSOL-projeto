package br.com.refood.controller;
import br.com.refood.model.*;
import br.com.refood.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/alimentos") @CrossOrigin
public class AlimentoController {
 private final AlimentoRepository alimentos; private final UsuarioRepository usuarios;
 public AlimentoController(AlimentoRepository a, UsuarioRepository u){alimentos=a;usuarios=u;}
 @GetMapping public List<Alimento> listar(@RequestParam(required=false) String status){
  if(status==null || status.isBlank()) return alimentos.findAll();
  return alimentos.findByStatus(StatusAlimento.valueOf(status.toUpperCase()));
 }
 @GetMapping("/{id}") public Alimento buscar(@PathVariable Long id){return alimentos.findById(id).orElseThrow();}
 @PostMapping public ResponseEntity<?> criar(@Valid @RequestBody Alimento a){
  if(a.getDoador()==null || a.getDoador().getId()==null) return ResponseEntity.badRequest().body("Informe o ID do usuário doador.");
  Usuario doador=usuarios.findById(a.getDoador().getId()).orElseThrow();
  if(doador.getTipo()!=TipoUsuario.DOADOR) return ResponseEntity.badRequest().body("O usuário precisa ser do tipo DOADOR.");
  if(a.getValidade()!=null && a.getValidade().isBefore(LocalDate.now())) return ResponseEntity.badRequest().body("Não é permitido cadastrar alimento com validade vencida.");
  a.setDoador(doador); a.setStatus(StatusAlimento.DISPONIVEL);
  return ResponseEntity.status(HttpStatus.CREATED).body(alimentos.save(a));
 }
 @GetMapping("/doador/{id}") public List<Alimento> porDoador(@PathVariable Long id){return alimentos.findByDoadorId(id);}
 @PatchMapping("/{id}/status") public Alimento atualizarStatus(@PathVariable Long id,@RequestParam StatusAlimento status){
  Alimento a=alimentos.findById(id).orElseThrow(); a.setStatus(status); return alimentos.save(a);
 }
}
