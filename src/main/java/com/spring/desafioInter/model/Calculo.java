package com.spring.desafioInter.model;

import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
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


}
