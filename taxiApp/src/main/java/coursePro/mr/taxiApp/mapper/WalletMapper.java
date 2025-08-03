package coursePro.mr.taxiApp.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import coursePro.mr.taxiApp.dto.WalletDto;
import coursePro.mr.taxiApp.entity.Wallet;

public class WalletMapper {

   public static WalletDto toDto(Wallet wallet) {
    WalletDto dto = new WalletDto();
    dto.setId(wallet.getId());
    dto.setSolde(wallet.getSolde());
    dto.setActif(wallet.getActif());
    dto.setDernierRechargement(wallet.getDernierRechargement());
    dto.setConducteurId(wallet.getConducteur().getId());
    dto.setTransactions(wallet.getTransactions() != null
        ? wallet.getTransactions().stream()
            .map(TransactionWalletMapper::toDto)
            .collect(Collectors.toList())
        : new ArrayList<>());
    return dto;
}


    public static Wallet toEntity(WalletDto dto) {
        Wallet wallet = new Wallet();
        wallet.setId(dto.getId());
        wallet.setSolde(dto.getSolde());
        wallet.setActif(dto.getActif());
        wallet.setDernierRechargement(dto.getDernierRechargement());
        return wallet;
    }
}
