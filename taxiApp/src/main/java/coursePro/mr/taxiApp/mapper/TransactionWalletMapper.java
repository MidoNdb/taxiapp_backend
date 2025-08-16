// TransactionWalletMapper.java
package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.entity.TransactionWallet;

public class TransactionWalletMapper {

    // Méthode principale avec wallet complet
    public static TransactionWalletDto toDto(TransactionWallet entity) {
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(entity.getId());
        dto.setMontant(entity.getMontant());
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setStatut(entity.getStatut() != null ? entity.getStatut().name() : null);
        dto.setDate(entity.getDate());
        dto.setPreuveUrl(entity.getPreuveUrl());

        if (entity.getWallet() != null) {
            // Utiliser la méthode sans transactions pour éviter la référence circulaire
            dto.setWallet(WalletMapper.toDtoWithoutTransactions(entity.getWallet()));
        }
        return dto;
    }

    // Méthode pour mapper une transaction SANS son wallet (pour éviter la référence circulaire)
    public static TransactionWalletDto toDtoWithoutWallet(TransactionWallet entity) {
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(entity.getId());
        dto.setMontant(entity.getMontant());
        dto.setType(entity.getType() != null ? entity.getType().name() : null);
        dto.setStatut(entity.getStatut() != null ? entity.getStatut().name() : null);
        dto.setDate(entity.getDate());
        dto.setPreuveUrl(entity.getPreuveUrl());

        // NE PAS mapper le wallet ici
        return dto;
    }

    public static TransactionWallet toEntity(TransactionWalletDto dto) {
        TransactionWallet entity = new TransactionWallet();
        entity.setId(dto.getId());
        entity.setMontant(dto.getMontant());
        entity.setPreuveUrl(dto.getPreuveUrl());
        // On ne met pas le wallet ici pour éviter un fetch complet non nécessaire
        return entity;
    }
}