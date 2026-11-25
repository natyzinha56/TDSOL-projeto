package br.com.refood.repository;
import br.com.refood.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> { boolean existsByEmail(String email); }
