package com.spring.desafioInter.model;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="CALCULO")
public class Calculo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @NotBlank
    @EqualsAndHashCode.Include
    private String numero;

    @NotBlank
    @EqualsAndHashCode.Include
    private Long quantidadeRepeticoes;

    @NotBlank
    private Long digitoUnico;


}
