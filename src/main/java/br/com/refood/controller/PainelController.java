package br.com.refood.controller;

import br.com.refood.model.*;
import br.com.refood.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController 
@RequestMapping("/api/painel") 
@CrossOrigin
public class PainelController {
    
    private final UsuarioRepository usuarios; 
    private final AlimentoRepository alimentos; 
    private final SolicitacaoRepository solicitacoes;

    public PainelController(UsuarioRepository u, AlimentoRepository a, SolicitacaoRepository s) {
        usuarios = u; 
        alimentos = a; 
        solicitacoes = s;
    }

    @GetMapping("/indicadores") 
    public Map<String, Object> indicadores() {
        long doados = alimentos.findAll().stream()
                .filter(a -> a.getStatus() == StatusAlimento.DOADO)
                .mapToLong(a -> a.getQuantidade() == null ? 0 : a.getQuantidade())
                .sum();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("usuarios", usuarios.count());
        resposta.put("estabelecimentos", usuarios.findAll().stream().filter(u -> u.getTipo() == TipoUsuario.DOADOR).count());
        resposta.put("instituicoes", usuarios.findAll().stream().filter(u -> u.getTipo() == TipoUsuario.INSTITUICAO).count());
        resposta.put("alimentosDisponiveis", (long) alimentos.findByStatus(StatusAlimento.DISPONIVEL).size());
        resposta.put("solicitacoes", solicitacoes.count());
        resposta.put("quantidadeItensDoados", doados);
        
        return resposta;
    }
}