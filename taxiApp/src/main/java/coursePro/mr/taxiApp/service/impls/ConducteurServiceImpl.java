package coursePro.mr.taxiApp.service.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.ConducteurRepository;
import coursePro.mr.taxiApp.dto.ConducteurDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Point;
import coursePro.mr.taxiApp.mapper.ConducteurMapper;
import coursePro.mr.taxiApp.service.ConducteurService;

@Service
public class ConducteurServiceImpl implements ConducteurService {

    @Autowired
    private ConducteurRepository conducteurRepo;

    @Override
    public ConducteurDto findById(Long id) {
        Conducteur conducteur = conducteurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
        return ConducteurMapper.toDto(conducteur);
    }

    @Override
    public List<ConducteurDto> findAll() {
        return conducteurRepo.findAll()
                .stream()
                .map(ConducteurMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConducteurDto save(ConducteurDto dto) {
        Conducteur entity = ConducteurMapper.toEntity(dto);
        Conducteur saved = conducteurRepo.save(entity);
        return ConducteurMapper.toDto(saved);
    }

    @Override
    public ConducteurDto update(ConducteurDto dto, Long id) {
        Conducteur existing = conducteurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur introuvable"));
        Conducteur c = ConducteurMapper.toEntity(dto);
        existing.setVehicule(c.getVehicule());
        existing.setNumeroPermis(c.getNumeroPermis());
        existing.setUtilisateur(c.getUtilisateur());
        existing.setCurentPosition(c.getCurentPosition());
        existing.setDisponible(c.getDisponible());
        conducteurRepo.save(existing);
        // Si besoin : mise à jour utilisateur
        return ConducteurMapper.toDto(conducteurRepo.save(existing));
    }

    @Override
    public void delete(Long id) {
        conducteurRepo.deleteById(id);
    }

    @Override
    public ConducteurDto updateLocation(Long id, Point location) {
         Conducteur existing = conducteurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur introuvable"));
        existing.setCurentPosition(location);
        conducteurRepo.save(existing);
         return ConducteurMapper.toDto(conducteurRepo.save(existing));
    }
}
