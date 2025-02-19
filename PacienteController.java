import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/PacienteControllerres")
public class PacienteController {

    private List<PacienteController> membros = newArrayList();

    //adicionar paciente
    @PostMapping("/add")
    public ResponseEntity<String> addMembro(@RequestBody PacienteController membro){
        membros.add(membro);
        return ResponseEntity.status(HttpStatus.CREATED).body("Membro da família adicionado com sucesso!");
      }
    
    //alterar paciente
    public ResponseEntity<String> alterarMembro(@PathVariable int id, @RequestBody PacienteController membroAtualizado){
        for(PacienteController membro : membros){
            if(membro.getId() ==id){
                membro.setNome(PacienteControllerrAtualizado.getNome());
                membro.setCpf(PacienteControllerrAtualizado.getCpf());
                membro.setDataNascimento(PacienteControllerrAtualizado.getDataNascimento());
                membro.setSexo(PacienteControllerrAtualizado.getSexo());
                return ResponseEntity.ok("Dados do PacienteControllerr alterado com sucesso!");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PacienteControllerr não encontrado!");
    }
    
    //excluir PacienteControllerr
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirMembro(@PathVariable int id){
        boolean removido = membros.removeIf(membro -> membro.getId() ==id);
        if(removido){
            return ResponseEntity.ok ("PacienteControllerr excluído com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOR_FOUND).body("PacienteControllerr não encontrado!");
    }

    //Listar PacienteControllerres
    @GetMapping("/listar")
    public ResponseEntity<List<PacienteController>> listarMembros(){
        return ResponseEntity.ok(membros);
    }
     
  
}
