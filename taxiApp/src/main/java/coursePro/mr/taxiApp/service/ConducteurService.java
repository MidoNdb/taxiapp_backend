package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.entity.Point;

public interface ConducteurService {

    ConducteurDto findById(Long id);
    ConducteurDto save(ConducteurDto conducteur);
    void delete(Long id);
    List<ConducteurDto> findAll();
    ConducteurDto update(ConducteurDto conducteur,Long id);
    ConducteurDto updateLocation(Long id, Point location);
    

}

