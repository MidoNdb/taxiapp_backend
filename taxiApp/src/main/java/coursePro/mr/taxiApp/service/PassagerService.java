package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.dto.PassagerDto;

public interface PassagerService {

    PassagerDto findById(Long id);
    //PassagerDto findByUtilisateurId(Long utilisateurId);
    List<PassagerDto> findAllPassager();
    
    PassagerDto save(PassagerDto passager);
    void delete(Long id);
    PassagerDto update(PassagerDto passager,Long id);
}

