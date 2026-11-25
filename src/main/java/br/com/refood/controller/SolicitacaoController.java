package br.com.refood.controller;
import br.com.refood.model.*;
import br.com.refood.repository.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/solicitacoes") @CrossOrigin
public class SolicitacaoController {
 private final SolicitacaoRepository solicitacoes; private final AlimentoRepository alimentos; private final UsuarioRepository usuarios;
 public SolicitacaoController(SolicitacaoRepository s,AlimentoRepository a,UsuarioRepository u){solicitacoes=s;alimentos=a;usuarios=u;}
 @GetMapping public List<Solicitacao> listar(){return solicitacoes.findAll();}
 @GetMapping("/instituicao/{id}") public List<Solicitacao> porInstituicao(@PathVariable Long id){return solicitacoes.findByInstituicaoId(id);}
 @PostMapping public ResponseEntity<?> criar(@RequestBody Solicitacao s){
  if(s.getAlimento()==null || s.getAlimento().getId()==null || s.getInstituicao()==null || s.getInstituicao().getId()==null)
   return ResponseEntity.badRequest().body("Informe alimento e instituição.");
  Alimento a=alimentos.findById(s.getAlimento().getId()).orElseThrow();
  Usuario i=usuarios.findById(s.getInstituicao().getId()).orElseThrow();
  if(i.getTipo()!=TipoUsuario.INSTITUICAO) return ResponseEntity.badRequest().body("Somente instituições podem solicitar doações.");
  if(a.getStatus()!=StatusAlimento.DISPONIVEL) return ResponseEntity.badRequest().body("Este alimento não está disponível.");
  if(s.getQuantidadeSolicitada()==null || s.getQuantidadeSolicitada()<1 || s.getQuantidadeSolicitada()>a.getQuantidade())
   return ResponseEntity.badRequest().body("Quantidade solicitada inválida.");
  s.setAlimento(a); s.setInstituicao(i); s.setStatus(StatusSolicitacao.PENDENTE);
  return ResponseEntity.status(HttpStatus.CREATED).body(solicitacoes.save(s));
 }
 @PatchMapping("/{id}/status") public ResponseEntity<?> atualizar(@PathVariable Long id,@RequestParam StatusSolicitacao status){
  Solicitacao s=solicitacoes.findById(id).orElseThrow();
  if(status==StatusSolicitacao.CONCLUIDA){
   Alimento a=s.getAlimento(); a.setStatus(StatusAlimento.DOADO); alimentos.save(a);
  }
  s.setStatus(status); return ResponseEntity.ok(solicitacoes.save(s));
 }
}
