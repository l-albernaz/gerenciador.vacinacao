import java.time.LocalDate;

public class Imunizacao {
    private int id, pacienteId, doseId;
    private LocalDate dataAplicacao;
    private String fabricante, lote, localAplicacao, profissionalAplicador;

    public Imunizacao(int id, int pacienteId, int doseId, LocalDate dataAplicacao, 
    String fabricante, String lote, String localAplicacao, String profissionalAplicador){
        this.id = id;
        this.pacienteId = pacienteId;
        this.doseId = doseId;
        this.dataAplicacao = dataAplicacao;
        this.fabricante = fabricante;
        this.lote = lote;
        this.localAplicacao = localAplicacao;
        this.profissionalAplicador = profissionalAplicador;
    }

    //Getters e Setters
    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public int getPacienteId(){return pacienteId;}
    public void setPacienteId(int pacienteId){this.pacienteId = pacienteId;}

    public int getDoseId(){return doseId;}
    public void setDoseId(int doseId){this.doseId = doseId;}

    public LocalDate getDataAplicacao(){return dataAplicacao;}
    public void setDataAplicacao(int dataAplicacao){this.dataAplicacao = dataAplicacao;}

    public String getFabricante(){return fabricante;}
    public void setFabricante(String fabricante){this.fabricante = fabricante;}

    public String getLote(){return lote;}
    public void setLote(String lote){this.lote = lote;}

    public String getLocalAplicacao(){return localAplicacao;}
    public void setLocalAplicacao(String localAplicacao){this.localAplicacao = localAplicacao;}

    public String getProfissionalAplicador(){return profissionalAplicador;}
    public void setprofissionalAplicador(String profissionalAplicador){this.profissionalAplicador = profissionalAplicador;}

}
