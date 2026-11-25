package br.com.refood.repository;
import br.com.refood.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AlimentoRepository extends JpaRepository<Alimento,Long> {
 List<Alimento> findByStatus(StatusAlimento status);
 List<Alimento> findByDoadorId(Long id);
}
