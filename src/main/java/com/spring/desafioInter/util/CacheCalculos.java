package com.spring.desafioInter.util;

import com.spring.desafioInter.model.Calculo;

public class CacheCalculos {

    public CacheCalculos() {
        this.tamanho = 0;
    }

    int TAMANHO_MAXIMO = 10;
    Calculo[] calculosRealizados = new Calculo[TAMANHO_MAXIMO];
    int tamanho;

    public Calculo desenfileirar(){

        if (calculosRealizados.length <= 0){
            return null;
        }

        Calculo calculo = calculosRealizados[0];
        for (int i=0; i<calculosRealizados.length -1; i++){
            calculosRealizados[i] = calculosRealizados[i+1];
        }
        tamanho--;
        return calculo;
    }

    public void enfileirar(Calculo calculo){

        if (tamanho >= TAMANHO_MAXIMO) {
            desenfileirar();
        }

        calculosRealizados[tamanho] = calculo;
        tamanho++;
    }

    public Calculo getCalculo(Calculo calculo){

        Calculo calculoRetorno = null;

        if (calculosRealizados != null
                && calculo.getNumero() != null
                && calculo.getQuantidadeRepeticoes() != null) {

            for (int i = 0; i < calculosRealizados.length; i++) {

                calculoRetorno = calculosRealizados[i];

                if (calculoRetorno != null
                        && calculo.getNumero().equals(calculoRetorno.getNumero())
                        && calculo.getQuantidadeRepeticoes().equals(calculoRetorno.getQuantidadeRepeticoes())) {
                    return calculoRetorno;
                }
            }
        }
        return null;
    }
}
