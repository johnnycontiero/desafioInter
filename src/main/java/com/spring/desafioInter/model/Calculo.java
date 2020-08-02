package com.spring.desafioInter.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="CALCULO")
public class Calculo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @NotBlank
    private String numero;

    @NotBlank
    private Long quantidadeRepeticoes;

    @NotBlank
    private Long digitoUnico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getQuantidadeRepeticoes() {
        return quantidadeRepeticoes;
    }

    public void setQuantidadeRepeticoes(Long quantidadeRepeticoes) {
        this.quantidadeRepeticoes = quantidadeRepeticoes;
    }

    public Long getDigitoUnico() {
        return digitoUnico;
    }

    public void setDigitoUnico(Long digitoUnico) {
        this.digitoUnico = digitoUnico;
    }
}
