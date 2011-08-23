package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientGlobal extends AbstractGlobal {

	@Override
	public AccounterConstants constants() {
		return Accounter.constants();
	}

	@Override
	public ClientCompanyPreferences preferences() {
		return Accounter.getCompany().getPreferences();
	}

	@Override
	public AccounterMessages messages() {
		return Accounter.messages();
	}

}
