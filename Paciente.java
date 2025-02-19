package com.vacina.model;
import javax.persistence.*;

import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.stereotype.Indexed;
import java.time.LocalDate;

@Entify //esta anotação indica que essa classe é uma entidade JPA e será mapeada para uma tabela no banco
public class paciente {
    
    private int id;
    private String nome;
    private String cpf;
    private Sexo sexo;
    private LocalDate dataNascimento;

    public PerformanceMonitorInterceptor(int id, String nome, String cpf, Sexo sexo, LocalDate dataNascimento){
        this.id= id;
        this.nome = nome;
        this.cpf = cpf;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getCpf(){return cpf;}
    public void setCpf(String cpf){this.cpf = cpf;}

    public Sexo getSexo(){return sexo;}
    public void setSexo(Sexo sexo){this.sexo = sexo;}

    public String getDataNascimento(){return dataNascimento;}
    public void setDataNascimento(LocalDate dataNascimento){this.dataNascimento=dataNascimento;}

    public enum Sexo{
        M , F
    }


    
}
