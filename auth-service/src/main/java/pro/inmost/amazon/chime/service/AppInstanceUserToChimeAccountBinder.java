package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;

public interface AppInstanceUserToChimeAccountBinder {

    AppInstanceUser bindAppInstanceUserToChimeAccount(ChimeAccount chimeAccount, AppInstanceUser appInstanceUser);
}
