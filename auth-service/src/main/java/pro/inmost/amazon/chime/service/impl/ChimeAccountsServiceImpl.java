package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;
import pro.inmost.amazon.chime.repositories.ChimeAccountRepository;
import pro.inmost.amazon.chime.service.ChimeAccountsService;
import pro.inmost.amazon.chime.service.ChimeClientService;
import software.amazon.awssdk.services.chime.model.CreateAccountRequest;
import software.amazon.awssdk.services.chime.model.CreateAccountResponse;
import java.util.Date;

@Service
public class ChimeAccountsServiceImpl implements ChimeAccountsService {

    @Autowired
    private ChimeClientService chimeClientService;

    @Autowired
    private ChimeAccountRepository repository;

    /**
     * @param accountName The name of the Amazon Chime account.
     * @return {@link ChimeAccount}
     */
    @Override
    public ChimeAccount createAccount(String accountName) {
        CreateAccountRequest createAccountRequest = CreateAccountRequest
                                                    .builder()
                                                    .name(accountName)
                                                    .build();

        CreateAccountResponse createAccountResponse = chimeClientService
                                .getAmazonChimeClient()
                                .createAccount(createAccountRequest);

        ChimeAccount account =  ChimeAccount.builder()
                .name(createAccountResponse.account().name())
                .accountId(createAccountResponse.account().accountId())
                .awsAccountId(Long.valueOf(createAccountResponse.account().awsAccountId()))
                .accountType(createAccountResponse.account().accountType().toString())
                .createdTimeStamp(new Date(createAccountResponse.account().createdTimestamp().getEpochSecond()))
                .defaultLicense(createAccountResponse.account().defaultLicenseAsString())
                .build();

        repository.save(account);
        return account;
    }

    public ChimeAccount findByName(String name) {
        return repository.findByName(name);
    }
    public ChimeAccount findById(Long id) {
        return repository.findById(id).get();
    }
}
