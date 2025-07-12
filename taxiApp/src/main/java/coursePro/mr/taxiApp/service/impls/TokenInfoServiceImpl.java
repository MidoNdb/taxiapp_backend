package coursePro.mr.taxiApp.service.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.TokenInfoRepo;
import coursePro.mr.taxiApp.entity.TokenInfo;
import coursePro.mr.taxiApp.service.TokenInfoService;

@Service
public class TokenInfoServiceImpl implements TokenInfoService {

    @Autowired
    private TokenInfoRepo tokenInfoRepository;

    @Override
    public TokenInfo save(TokenInfo tokenInfo) {
        return tokenInfoRepository.save(tokenInfo);
    }

    @Override
    public Optional<TokenInfo> findTokenInfo(String refreshToken) {
        return tokenInfoRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public void deleteById(Long id) {
        tokenInfoRepository.deleteById(id);
    }

    @Override
    public List<TokenInfo> findByUserId(Long userId) {
        return tokenInfoRepository.findByUserId(userId);
    }
}

