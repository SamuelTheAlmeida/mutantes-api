/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;

/**
 *
 * @author SAMUEL
 */
public class Habilidade implements java.io.Serializable{
    private int id;
    private String descricao;
    private Mutante mutante;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Mutante getMutante() {
        return mutante;
    }

    public void setMutante(Mutante mutante) {
        this.mutante = mutante;
    }  
    
}
