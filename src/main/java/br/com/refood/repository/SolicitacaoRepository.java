package br.com.refood.repository;
import br.com.refood.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SolicitacaoRepository extends JpaRepository<Solicitacao,Long> {
 List<Solicitacao> findByInstituicaoId(Long id);
}
