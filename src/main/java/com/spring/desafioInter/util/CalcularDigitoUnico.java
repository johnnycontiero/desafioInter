package com.spring.desafioInter.util;

import java.util.stream.LongStream;

public class CalcularDigitoUnico {

    public Long calcular(String numero, Long quantidadeRepeticoes){

        Long digitoUnico = Long.valueOf(0);

        if (quantidadeRepeticoes != null && quantidadeRepeticoes > 0 && numero != null) {

            if (numero.length() > 1) {

                Long contador;

                while (numero.length() > 1) {

                    contador = Long.valueOf(0);
                    int[] digitos = numero.chars().map(Character::getNumericValue).toArray();

                    for(int c : digitos){
                        contador = Long.sum(contador, Long.valueOf(c));
                    }
                    contador = contador * quantidadeRepeticoes;
                    quantidadeRepeticoes = Long.valueOf(1);

                    numero = contador.toString();
                }
                digitoUnico = Long.valueOf(numero);

            } else {
                digitoUnico = Long.valueOf(numero);
            }

        }
        System.out.println("Digito Único calculado: " + digitoUnico);
        return digitoUnico;
    }
}