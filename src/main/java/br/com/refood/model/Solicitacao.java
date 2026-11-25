package br.com.refood.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="solicitacoes")
public class Solicitacao {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Alimento alimento;
 @ManyToOne(optional=false) private Usuario instituicao;
 private Integer quantidadeSolicitada;
 @Enumerated(EnumType.STRING) private StatusSolicitacao status=StatusSolicitacao.PENDENTE;
 private LocalDateTime criadaEm=LocalDateTime.now();
 @Column(length=1000) private String observacao;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Alimento getAlimento(){return alimento;} public void setAlimento(Alimento v){alimento=v;}
 public Usuario getInstituicao(){return instituicao;} public void setInstituicao(Usuario v){instituicao=v;}
 public Integer getQuantidadeSolicitada(){return quantidadeSolicitada;} public void setQuantidadeSolicitada(Integer v){quantidadeSolicitada=v;}
 public StatusSolicitacao getStatus(){return status;} public void setStatus(StatusSolicitacao v){status=v;}
 public LocalDateTime getCriadaEm(){return criadaEm;} public void setCriadaEm(LocalDateTime v){criadaEm=v;}
 public String getObservacao(){return observacao;} public void setObservacao(String v){observacao=v;}
}
