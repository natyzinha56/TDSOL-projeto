package br.com.refood.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity @Table(name="alimentos")
public class Alimento {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank private String nome;
 @Column(length=1000) private String descricao;
 @Min(1) private Integer quantidade;
 @NotBlank private String unidade;
 private LocalDate validade;
 private LocalDate dataCadastro=LocalDate.now();
 private LocalTime horarioRetirada;
 @NotBlank private String condicoesConservacao;
 private String fotoUrl;
 @Enumerated(EnumType.STRING) private StatusAlimento status=StatusAlimento.DISPONIVEL;
 @ManyToOne(optional=false) private Usuario doador;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getNome(){return nome;} public void setNome(String v){nome=v;}
 public String getDescricao(){return descricao;} public void setDescricao(String v){descricao=v;}
 public Integer getQuantidade(){return quantidade;} public void setQuantidade(Integer v){quantidade=v;}
 public String getUnidade(){return unidade;} public void setUnidade(String v){unidade=v;}
 public LocalDate getValidade(){return validade;} public void setValidade(LocalDate v){validade=v;}
 public LocalDate getDataCadastro(){return dataCadastro;} public void setDataCadastro(LocalDate v){dataCadastro=v;}
 public LocalTime getHorarioRetirada(){return horarioRetirada;} public void setHorarioRetirada(LocalTime v){horarioRetirada=v;}
 public String getCondicoesConservacao(){return condicoesConservacao;} public void setCondicoesConservacao(String v){condicoesConservacao=v;}
 public String getFotoUrl(){return fotoUrl;} public void setFotoUrl(String v){fotoUrl=v;}
 public StatusAlimento getStatus(){return status;} public void setStatus(StatusAlimento v){status=v;}
 public Usuario getDoador(){return doador;} public void setDoador(Usuario v){doador=v;}
}
