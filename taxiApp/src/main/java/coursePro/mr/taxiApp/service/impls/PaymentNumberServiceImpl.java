package coursePro.mr.taxiApp.service.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coursePro.mr.taxiApp.dao.PaymentNumberRepository;
import coursePro.mr.taxiApp.dto.PaymentNumberDto;
import coursePro.mr.taxiApp.entity.PaymentNumber;
import coursePro.mr.taxiApp.mapper.PaymentNumberMapper;
import coursePro.mr.taxiApp.service.PaymentNumberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@Transactional
public class PaymentNumberServiceImpl implements PaymentNumberService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentNumberServiceImpl.class);
    
    private final PaymentNumberRepository paymentNumberRepository;
    private final PaymentNumberMapper paymentNumberMapper;
    
    public PaymentNumberServiceImpl(PaymentNumberRepository paymentNumberRepository, 
                                   PaymentNumberMapper paymentNumberMapper) {
        this.paymentNumberRepository = paymentNumberRepository;
        this.paymentNumberMapper = paymentNumberMapper;
    }
    
  @Override
@Transactional(readOnly = true)
public List<PaymentNumberDto> getOperateursDisponibles() {
    try {
        List<PaymentNumberDto> dtos = new ArrayList<>();
        List<PaymentNumber> pns = paymentNumberRepository.findAll();
        
        for (PaymentNumber elem : pns) {
            PaymentNumberDto p = paymentNumberMapper.toDto(elem); // Ajout de 'paymentNumberMapper'
            dtos.add(p); // Ajout de 'p' dans la méthode add()
        }
        
        return dtos; // Retour de la liste dtos
        
    } catch (Exception e) {
        logger.error("Erreur lors de la récupération des opérateurs", e);
        return new ArrayList<>();
    }
}
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentNumberDto> getNumerosParOperateur(String operateur) {
        if (operateur == null || operateur.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            String operateurNormalise = normaliserOperateur(operateur);
            List<PaymentNumber> numeros = paymentNumberRepository.findByOperateurAndActifTrue(operateurNormalise);
            return paymentNumberMapper.toDtoListForMobile(numeros);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des numéros pour l'opérateur: {}", operateur, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentNumberDto> getNumeroForMobile(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        
        try {
            Optional<PaymentNumber> numero = paymentNumberRepository.findByIdAndActifTrue(id);
            if (numero.isPresent()) {
                PaymentNumberDto dto = paymentNumberMapper.toDtoForMobile(numero.get());
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du numéro avec l'ID: {}", id, e);
            return Optional.empty();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isNumeroActif(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            return paymentNumberRepository.findByIdAndActifTrue(id).isPresent();
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du numéro avec l'ID: {}", id, e);
            return false;
        }
    }
    
    @Override
    public PaymentNumberDto creerNumero(PaymentNumberDto dto) {
        if (dto == null) {
            return null;
        }
        
        try {
            String operateurNormalise = normaliserOperateur(dto.getOperateur());
            
            // Vérifier si le numéro existe déjà
            if (numeroExiste(dto.getNumeroTelephone(), operateurNormalise)) {
                logger.warn("Le numéro {} existe déjà pour l'opérateur {}", dto.getNumeroTelephone(), operateurNormalise);
                return null;
            }
            
            PaymentNumber entity = paymentNumberMapper.toEntity(dto);
            entity.setOperateur(operateurNormalise);
            
            PaymentNumber savedEntity = paymentNumberRepository.save(entity);
            return paymentNumberMapper.toDto(savedEntity);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la création du numéro", e);
            return null;
        }
    }
    
    @Override
    public PaymentNumberDto modifierNumero(Long id, PaymentNumberDto dto) {
        if (id == null || dto == null) {
            return null;
        }
        
        try {
            Optional<PaymentNumber> existingOptional = paymentNumberRepository.findById(id);
            if (!existingOptional.isPresent()) {
                logger.warn("Numéro non trouvé avec l'ID: {}", id);
                return null;
            }
            
            PaymentNumber existingEntity = existingOptional.get();
            String operateurNormalise = normaliserOperateur(dto.getOperateur());
            
            // Vérifier si le numéro modifié existe déjà (sauf pour le numéro actuel)
            boolean numeroExisteAilleurs = paymentNumberRepository.existsByNumeroTelephoneAndOperateur(
                    dto.getNumeroTelephone(), operateurNormalise) 
                    && !existingEntity.getNumeroTelephone().equals(dto.getNumeroTelephone());
            
            if (numeroExisteAilleurs) {
                logger.warn("Le numéro {} existe déjà pour l'opérateur {}", dto.getNumeroTelephone(), operateurNormalise);
                return null;
            }
            
            paymentNumberMapper.updateEntityFromDto(existingEntity, dto);
            existingEntity.setOperateur(operateurNormalise);
            
            PaymentNumber savedEntity = paymentNumberRepository.save(existingEntity);
            return paymentNumberMapper.toDto(savedEntity);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la modification du numéro avec l'ID: {}", id, e);
            return null;
        }
    }
    
    @Override
    public PaymentNumberDto changerStatutNumero(Long id, boolean actif) {
        if (id == null) {
            return null;
        }
        
        try {
            Optional<PaymentNumber> entityOptional = paymentNumberRepository.findById(id);
            if (!entityOptional.isPresent()) {
                logger.warn("Numéro non trouvé avec l'ID: {}", id);
                return null;
            }
            
            PaymentNumber entity = entityOptional.get();
            entity.setActif(actif);
            PaymentNumber savedEntity = paymentNumberRepository.save(entity);
            
            return paymentNumberMapper.toDto(savedEntity);
            
        } catch (Exception e) {
            logger.error("Erreur lors du changement de statut du numéro avec l'ID: {}", id, e);
            return null;
        }
    }
    
    @Override
    public void supprimerNumero(Long id) {
        if (id == null) {
            return;
        }
        
        try {
            if (paymentNumberRepository.existsById(id)) {
                paymentNumberRepository.deleteById(id);
                logger.info("Numéro supprimé avec succès. ID: {}", id);
            } else {
                logger.warn("Numéro non trouvé pour suppression avec l'ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du numéro avec l'ID: {}", id, e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentNumberDto> getAllNumeros() {
        try {
            List<PaymentNumber> numeros = paymentNumberRepository.findAll();
            return paymentNumberMapper.toDtoList(numeros);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les numéros", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentNumberDto> getNumeroById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        
        try {
            Optional<PaymentNumber> numero = paymentNumberRepository.findById(id);
            if (numero.isPresent()) {
                PaymentNumberDto dto = paymentNumberMapper.toDto(numero.get());
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du numéro avec l'ID: {}", id, e);
            return Optional.empty();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PaymentNumberDto> rechercherNumeros(String operateur, Boolean actif, String nomProprietaire) {
        try {
            List<PaymentNumber> numeros = paymentNumberRepository.findAll();
            
            List<PaymentNumber> filteredNumeros = numeros.stream()
                    .filter(numero -> operateur == null || 
                            normaliserOperateur(operateur).equals(numero.getOperateur()))
                    .filter(numero -> actif == null || actif.equals(numero.getActif()))
                    .filter(numero -> nomProprietaire == null || 
                            (numero.getNomProprietaire() != null && 
                             numero.getNomProprietaire().toLowerCase().contains(nomProprietaire.toLowerCase())))
                    .collect(Collectors.toList());
            
            return paymentNumberMapper.toDtoList(filteredNumeros);
                    
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de numéros", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean numeroExiste(String numeroTelephone, String operateur) {
        if (numeroTelephone == null || operateur == null) {
            return false;
        }
        
        try {
            String operateurNormalise = normaliserOperateur(operateur);
            return paymentNumberRepository.existsByNumeroTelephoneAndOperateur(numeroTelephone, operateurNormalise);
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification d'existence du numéro", e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countNumerosActifsParOperateur(String operateur) {
        if (operateur == null) {
            return 0L;
        }
        
        try {
            String operateurNormalise = normaliserOperateur(operateur);
            Long count = paymentNumberRepository.countByOperateurAndActifTrue(operateurNormalise);
            return count != null ? count : 0L;
        } catch (Exception e) {
            logger.error("Erreur lors du comptage des numéros actifs pour l'opérateur: {}", operateur, e);
            return 0L;
        }
    }
    
    private String normaliserOperateur(String operateur) {
        if (operateur == null) {
            return null;
        }
        return operateur.toLowerCase().trim();
    }
}