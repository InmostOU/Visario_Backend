package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.entity.ChimeAccount;

/**
 * Creates an Amazon Chime account under the administrator's AWS account.
 * Only Team account types are currently supported for this action.
 * For more information about different account types, see <a href="#{@https://docs.aws.amazon.com/chime/latest/ag/manage-chime-account.html}">Managing Your Amazon Chime Accounts in the Amazon Chime Administration Guide.</a>
 */

public interface ChimeAccountsService {
    /**
     * @param accountName   The name of the Amazon Chime account.
     * @return  {@link ChimeAccount}
     */
    ChimeAccount createAccount(String accountName);

    ChimeAccount findByName(String name);

    ChimeAccount findById(Long id);
}
