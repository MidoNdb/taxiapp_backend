package coursePro.mr.taxiApp.mapper;


import org.springframework.stereotype.Component;

import coursePro.mr.taxiApp.dto.PaymentNumberDto;
import coursePro.mr.taxiApp.entity.PaymentNumber;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentNumberMapper {
    
    // Entity vers DTO
    public PaymentNumberDto toDto(PaymentNumber entity) {
        if (entity == null) {
            return null;
        }
        
        PaymentNumberDto dto = new PaymentNumberDto();
        dto.setId(entity.getId());
        dto.setNumeroTelephone(entity.getNumeroTelephone());
        dto.setOperateur(entity.getOperateur());
        dto.setNomProprietaire(entity.getNomProprietaire());
        dto.setActif(entity.getActif());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateModification(entity.getDateModification());
        
        // Champs calculés
        if (entity.getOperateur() != null) {
            dto.setOperateurAffichage(capitalizeOperateur(entity.getOperateur()));
            dto.setCouleurOperateur(getCouleurOperateur(entity.getOperateur()));
            dto.setIconeOperateur(getIconeOperateur(entity.getOperateur()));
            dto.setInstructions(generateInstructions(entity.getOperateur()));
        }
        
        if (entity.getNumeroTelephone() != null) {
            dto.setFormatAffichage(formatNumero(entity.getNumeroTelephone()));
        }
        
        dto.setDisponible(entity.getActif());
        
        return dto;
    }
    
    // DTO vers Entity (pour création)
    public PaymentNumber toEntity(PaymentNumberDto dto) {
        if (dto == null) {
            return null;
        }
        
        PaymentNumber entity = new PaymentNumber();
        entity.setNumeroTelephone(dto.getNumeroTelephone());
        entity.setNomProprietaire(dto.getNomProprietaire());
        
        if (dto.getOperateur() != null) {
            entity.setOperateur(normaliserOperateur(dto.getOperateur()));
        }
        
        if (dto.getActif() != null) {
            entity.setActif(dto.getActif());
        } else {
            entity.setActif(true);
        }
        
        return entity;
    }
    
    // Mise à jour d'une entity existante avec un DTO
    public void updateEntityFromDto(PaymentNumber entity, PaymentNumberDto dto) {
        if (entity == null || dto == null) {
            return;
        }
        
        entity.setNumeroTelephone(dto.getNumeroTelephone());
        entity.setNomProprietaire(dto.getNomProprietaire());
        
        if (dto.getOperateur() != null) {
            entity.setOperateur(normaliserOperateur(dto.getOperateur()));
        }
        
        if (dto.getActif() != null) {
            entity.setActif(dto.getActif());
        }
    }
    
    // DTO version mobile (avec informations d'affichage enrichies)
    public PaymentNumberDto toDtoForMobile(PaymentNumber entity) {
        PaymentNumberDto dto = toDto(entity);
        if (dto != null && entity.getOperateur() != null) {
            // Enrichir avec des informations spécifiques mobile
            dto.setInstructions(generateDetailedInstructions(entity.getOperateur()));
        }
        return dto;
    }
    
    // Listes
    public List<PaymentNumberDto> toDtoList(List<PaymentNumber> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<PaymentNumberDto> toDtoListForMobile(List<PaymentNumber> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDtoForMobile)
                .collect(Collectors.toList());
    }
    
    // === MÉTHODES UTILITAIRES PRIVÉES ===
    
    private String normaliserOperateur(String operateur) {
        if (operateur == null) {
            return null;
        }
        return operateur.toLowerCase().trim();
    }
    
    private String capitalizeOperateur(String operateur) {
        if (operateur == null || operateur.isEmpty()) {
            return operateur;
        }
        
        switch (operateur.toLowerCase()) {
            case "bankily":
                return "Bankily";
            case "masrif":
                return "Masrif";
            case "sedad":
                return "Sedad";
            case "bimbank":
                return "BimBank";
            case "bmci":
                return "BMCI";
            default:
                return operateur.substring(0, 1).toUpperCase() + operateur.substring(1).toLowerCase();
        }
    }
    
    private String getIconeOperateur(String operateur) {
        switch (normaliserOperateur(operateur)) {
            case "bankily":
                return "ic_bankily.png";
            case "masrif":
                return "ic_masrif.png";
            case "sedad":
                return "ic_sedad.png";
            case "bimbank":
                return "ic_bimbank.png";
            case "bmci":
                return "ic_bmci.png";
            default:
                return "ic_mobile_money.png";
        }
    }
    
    private String getCouleurOperateur(String operateur) {
        switch (normaliserOperateur(operateur)) {
            case "bankily":
                return "#FF6B35";
            case "masrif":
                return "#2E86AB";
            case "sedad":
                return "#A23B72";
            case "bimbank":
                return "#F18F01";
            case "bmci":
                return "#C73E1D";
            default:
                return "#6C757D";
        }
    }
    
    private String formatNumero(String numero) {
        if (numero == null || numero.length() != 8) {
            return numero;
        }
        // Format: XX XX XX XX
        return String.format("%s %s %s %s", 
                numero.substring(0, 2),
                numero.substring(2, 4),
                numero.substring(4, 6),
                numero.substring(6, 8));
    }
    
    private String generateInstructions(String operateur) {
        String operateurCapitalized = capitalizeOperateur(operateur);
        return String.format("Transférez vers ce numéro %s", operateurCapitalized);
    }
    
    private String generateDetailedInstructions(String operateur) {
        String operateurCapitalized = capitalizeOperateur(operateur);
        return String.format(
            "1. Ouvrez votre application %s\n" +
            "2. Sélectionnez 'Transfert d'argent'\n" +
            "3. Saisissez le numéro affiché\n" +
            "4. Entrez le montant exact\n" +
            "5. Confirmez la transaction\n" +
            "6. Prenez une capture d'écran de la confirmation",
            operateurCapitalized
        );
    }
}