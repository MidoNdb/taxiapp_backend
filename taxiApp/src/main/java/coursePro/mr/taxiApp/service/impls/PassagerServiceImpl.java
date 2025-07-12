package coursePro.mr.taxiApp.service.impls;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.PassagerRepository;
import coursePro.mr.taxiApp.dto.PassagerDto;
import coursePro.mr.taxiApp.entity.Passager;
import coursePro.mr.taxiApp.mapper.PassagerMapper;
import coursePro.mr.taxiApp.service.PassagerService;

@Service
public class PassagerServiceImpl implements PassagerService {

    @Autowired
    private PassagerRepository repo;

    @Override
    public PassagerDto findById(Long id) {
       
       Passager passager= repo.findById(id).orElseThrow();
       PassagerDto passagerDto = PassagerMapper.toDto(passager);
        return  passagerDto;
        
         
    }

    // @Override
    // public Passager findByUtilisateurId(Long utilisateurId) {
    //     return repo.findByUtilisateurId(utilisateurId).orElseThrow();
    // }

    @Override
    public PassagerDto save(PassagerDto dto) {
        Passager passager = PassagerMapper.toEntity(dto);

        Passager p = repo.save(passager);

        return PassagerMapper.toDto(p);
    }


    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<PassagerDto> findAllPassager() {
        List<PassagerDto> passagerDtos= new ArrayList<>();
       List<Passager> ps = repo.findAll();
       for (Passager p : ps) {
        PassagerDto passagerDto = PassagerMapper.toDto(p);
        passagerDtos.add(passagerDto);
  
       }
       return passagerDtos; 
    }


    @Override
    public PassagerDto update(PassagerDto dto, Long id) {
        Passager entity = repo.findById(id).orElseThrow();
        if (dto.getAdresse() != null) entity.setAdresse(dto.getAdresse());
        //if (dto.getUtilisateur() != null) entity.setUtilisateur(PassagerMapper.toEntity(dto).getUtilisateur());
        return PassagerMapper.toDto(repo.save(entity));
    }
    // public PassagerDto update(PassagerDto dto,Long id) {
    //      Passager passager = PassagerMapper.toEntity(dto);
    //      Optional<Passager> optp = repo.findById(id);
    //      if (optp.isPresent()) {
    //         Passager p =optp.get();
    //         p.setAdresse(passager.getAdresse());
    //         Utilisateur user = 
    //         p.setUtilisateur(utilisateur);
    //      }

    //     Passager pass = repo.save(passager);
        
        
    // }
}

