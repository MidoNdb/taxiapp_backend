package coursePro.mr.taxiApp.service;


import java.util.Optional;
import java.util.List;

import coursePro.mr.taxiApp.entity.TokenInfo;

public interface TokenInfoService {

    TokenInfo save(TokenInfo tokenInfo);

    Optional<TokenInfo> findTokenInfo(String refreshToken);

    void deleteById(Long id);

    List<TokenInfo> findByUserId(Long userId); // Optionnel pour voir tous les tokens d'un utilisateur
}

