package coursePro.mr.taxiApp.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Wallet;

public class WalletMapper {
    
    // Méthode principale avec transactions complètes
    public static WalletDto toDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setSolde(wallet.getSolde());
        dto.setActif(wallet.getActif());
        dto.setDernierRechargement(wallet.getDernierRechargement());

        if (wallet.getConducteur() != null) {
            dto.setConducteur(ConducteurMapper.toDto(wallet.getConducteur()));
        }

        // Mapper les transactions SANS leur wallet pour éviter la référence circulaire
        dto.setTransactions(wallet.getTransactions() != null
            ? wallet.getTransactions().stream()
                .map(TransactionWalletMapper::toDtoWithoutWallet)  // ← Nouvelle méthode
                .collect(Collectors.toList())
            : new ArrayList<>());
        return dto;
    }

    // Méthode pour mapper un wallet SANS ses transactions (pour éviter la référence circulaire)
    public static WalletDto toDtoWithoutTransactions(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setSolde(wallet.getSolde());
        dto.setActif(wallet.getActif());
        dto.setDernierRechargement(wallet.getDernierRechargement());

        if (wallet.getConducteur() != null) {
            dto.setConducteur(ConducteurMapper.toDto(wallet.getConducteur()));
        }

        // NE PAS mapper les transactions ici
        dto.setTransactions(new ArrayList<>());
        return dto;
    }

    public static Wallet toEntity(WalletDto dto) {
        Wallet wallet = new Wallet();
        wallet.setId(dto.getId());
        wallet.setSolde(dto.getSolde());
        wallet.setActif(dto.getActif());
        wallet.setDernierRechargement(dto.getDernierRechargement());

        if (dto.getConducteur() != null) {
            wallet.setConducteur(ConducteurMapper.toEntity(dto.getConducteur()));
        }
        return wallet;
    }
}