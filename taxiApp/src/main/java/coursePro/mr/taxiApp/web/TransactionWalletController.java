// package coursePro.mr.taxiApp.web;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import coursePro.mr.taxiApp.dto.ConducteurDto;
// import coursePro.mr.taxiApp.dto.TransactionWalletDto;
// import coursePro.mr.taxiApp.dto.WalletDto;
// import coursePro.mr.taxiApp.entity.Conducteur;
// import coursePro.mr.taxiApp.security.JwtService;
// import coursePro.mr.taxiApp.service.TransactionWalletService;
// import coursePro.mr.taxiApp.service.WalletService;
// import jakarta.servlet.http.HttpServletRequest;
// import java.nio.file.Files;
// import java.nio.file.StandardCopyOption;
// import java.io.File;
// import java.io.IOException;
// import java.util.List;
// @RestController
// @RequestMapping("/wallet")
// public class TransactionWalletController {

//     private final WalletService walletService;
//     private final TransactionWalletService transactionWalletService;

//     @Value("${app.upload.dir:uploads/preuves}")
//     private String uploadDir;

//     @Autowired
//     private JwtService jwtService;

//     public TransactionWalletController(WalletService walletService, TransactionWalletService transactionWalletService) {
//         this.walletService = walletService;
//         this.transactionWalletService = transactionWalletService;
//     }

//     @GetMapping("/conducteur/transactions")
//     public ResponseEntity<List<TransactionWalletDto>> getMesTransactions(HttpServletRequest request) {
//         try {
//             String authHeader = request.getHeader("Authorization");
//             if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                 return ResponseEntity.status(401).build();
//             }

//             String token = authHeader.substring(7);
//             Long id = jwtService.extractUserId(token);
            
//             ConducteurDto conducteur = new ConducteurDto(id);
//             WalletDto wallet = walletService.getOrCreateWallet(conducteur);
//             List<TransactionWalletDto> transactions = transactionWalletService.getTransactionsParWallet(wallet);
            
//             return ResponseEntity.ok(transactions);
//         } catch (Exception e) {
//             System.err.println("❌ Erreur getMesTransactions: " + e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.status(500).build();
//         }
//     }

// }