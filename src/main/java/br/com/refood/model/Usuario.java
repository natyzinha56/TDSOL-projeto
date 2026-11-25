package br.com.refood.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="usuarios")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String nome;
 @Email @NotBlank @Column(unique=true) private String email;
 @NotBlank private String telefone;
 @NotBlank private String endereco;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TipoUsuario tipo;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getNome(){return nome;} public void setNome(String v){nome=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getTelefone(){return telefone;} public void setTelefone(String v){telefone=v;}
 public String getEndereco(){return endereco;} public void setEndereco(String v){endereco=v;}
 public TipoUsuario getTipo(){return tipo;} public void setTipo(TipoUsuario v){tipo=v;}
}
