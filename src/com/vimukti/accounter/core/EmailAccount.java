package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class EmailAccount extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140130664346058589L;

	private String emailId;
	private String password;
	private String smtpMailServer;
	private int portNumber;
	private boolean isSSL;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpMailServer() {
		return smtpMailServer;
	}

	public void setSmtpMailServer(String smtpMailServer) {
		this.smtpMailServer = smtpMailServer;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.email(), this.emailId);

		w.put(messages.portNumber(), this.portNumber);

		w.put(messages.smtpMailServer(), this.smtpMailServer);

	}

	@Override
	public String getName() {
		return getEmailId();
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public int getObjType() {
		return IAccounterCore.EMAIL_ACCOUNT;
	}

}
