import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/imunizacao")
public class ImunizacaoController {

   private final List<Imunizacao> imunizacoes = new ArrayList<>();

   //Adicionar imunização
   @PostMapping("/inserir")
   public ResponseEntity<String> adicionarImunizacao(@RequestBody Imunizacao imunizacao){
    imunizacoes.add(imunizacao);
    return ResponseEntity.status(HttpStatus.CREATED).body("Imunização registrada com sucesso!");
    }

    //Alterar dados de uma imunização
    @PutMapping("/alterar/{id}")
    public ResponseEntity<String> alterarImunizacao(@PathVariable int id, @RequestBody Imunizacao imunizacaoAtualizada){
        for(Imunizacao imunizacao : imunizacoes){
            if(imunizacao.getId() == id){
                imunizacao.setPacienteId(imunizacaoAtualizada.getPacienteId());
                imunizacao.setDoseId(imunizacaoAtualizada.getDoseId());
                imunizacao.setDataAplicacao(imunizacaoAtualizada.getDataAplicacao());
                imunizacao.setFabricante(im,unizacaoAtualizada.getFabricante());
                imunizacao.setLote(imunizacaoAtualizada.getLote());
                imunizacao.setLocalAplicacao(imunizacaoAtualizada.getLocalAplicacao());
                imunizacao.setProfissionalAplicador(imunizacaoAtualizada.getProfissionalAplicador());
                return ResponseEntity.ok("Imunização alterada com sucesso!");
               }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imunização não encontrada!");   
    }

    //Excluir uma imunização
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirImunizacao(@PathVariable int id){
        boolean removido = imunizacoes.removeIf(imunizacao -> imunizacao.getId() == id);
        if(removido){
            return ResponseEntity.ok("Imunização excluída com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imunização não encontrada!");
    }

    //Excluir todas as imunizações de um paciente
    @DeleteMapping("/excluir/paciente/{id}")
    public ResponseEntity<String> excluirImunizacaoPorPaciente(@PathVariable int id){
        boolean removidos = imunizacoes.removeIf( imunizacao -> imunizacao.getPacienteId()==id);
        if(removidos){
            return ResponseEntity.ok("Todas as imunizações do paciente foram excluídas com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma imunização encontrada para este paciente!");
    }

    //Consultar todas as imunizações
    @GetMapping("/consultar")
    public ResponseEntity<List<Imunizacao>> listarImunizacoes(){
        return ResponseEntity.ok(imunizacoes);
    }

    //Consultar imunizaçaõ por id da imunização
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Imunizacao> consultarImunizacaoPorId(@PathVariable int id){
        return imunizacoes.stream()
                .filter(imunizacao -> imunizacao.getId()==id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    //Consultar imunização por id do paciente
    @GetMapping("/consultar/paciente/{id}")
    public ResponseEntity<List<Imunizacao>> consultarImunizacoesPorPaciente(@PathVariable int id){
        List<Imunizacao> resultado = newArrayList();
        for(Imunizacao imunizacao : imunizacoes){
            if(imunizacao.getPacienteId()==id){
                resultado.add(imunizacao);
            }
        }
        return ResponseEntity.ok(resultado);
    }

    //Consultar imunizações por id do paciente e intervalo de aplicação
    @GetMapping("/consultar/paciente/{id}/aplicacao/{dtIni}/{dtFim}")
    public ResponseEntity<List<Imunizacao>> consultarImunizacaoPorPeriodo(
        @PathVariable int id, @PathVariable String dtIni, @PathVariable String dtFim){

            LocalDate dataInicial = Localdate.parse(dtIni);
            LocalDate dataFinal = LocalDate.parse(dtFim);
            List<Imunizacao> resultado = new ArrayList();

            for(imunizacao imunizacao: imunizacoes){
                if(imunizacao.getPacienteId() == id &&
                (imunizacao.getDataAplicacao().isAfter(dataInicial) || imunizacao.getDataAplicacao().isEqual(dataInicial)) &&
                (imunizacao.getDataAplicacao().isBefore(dataFinal) || imunizacao.getDataAplicacao().isEqual(dataFinal))){
                    resultado.add(imunizacao);
                }
            }
            return ResponseEntity.ok(resultado);
        }

 
}
