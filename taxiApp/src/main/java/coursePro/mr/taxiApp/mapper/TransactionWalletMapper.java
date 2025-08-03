package coursePro.mr.taxiApp.mapper;

import coursePro.mr.taxiApp.dto.TransactionWalletDto;
import coursePro.mr.taxiApp.entity.TransactionWallet;

public class TransactionWalletMapper {
    public static TransactionWalletDto toDto(TransactionWallet entity) {
        TransactionWalletDto dto = new TransactionWalletDto();
        dto.setId(entity.getId());
        dto.setMontant(entity.getMontant());
        dto.setType(entity.getType().name());
        dto.setStatut(entity.getStatut().name());
        dto.setDate(entity.getDate());
        dto.setPreuveUrl(entity.getPreuveUrl());
        return dto;
    }

    public static TransactionWallet toEntity(TransactionWalletDto dto) {
        TransactionWallet entity = new TransactionWallet();
        entity.setId(dto.getId());
        entity.setMontant(dto.getMontant());
        entity.setPreuveUrl(dto.getPreuveUrl());
        return entity;
    }
}
