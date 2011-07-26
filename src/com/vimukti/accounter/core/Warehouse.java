/**
 * 
 */
package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

/**
 * Warehouse POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class Warehouse extends CreatableObject implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 640523202925694992L;

	private Address address;
	private Set<ItemStatus> itemStatuses;

	private String name;
	private Contact contact;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	private transient boolean isOnSaveProccessed;

	public Warehouse() {
	}

	public Address getAddress() {
		return address;
	}

	public Set<ItemStatus> getItemStatuses() {
		return itemStatuses;
	}

	public String getName() {
		return name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setItemStatuses(Set<ItemStatus> itemStatuses) {
		this.itemStatuses = itemStatuses;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Contact getContact() {
		return contact;
	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (isOnSaveProccessed)
			return true;

		isOnSaveProccessed = true;

		FinanceLogger.log("warehouse has been created: {0}", name);

		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {

		FinanceLogger.log("Warehouse deleted: {0}", name);

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setObjectType(AccounterCoreType.WAREHOUSE);
		ChangeTracker.put(accounterCore);

		return false;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onLoad(Session s, Serializable id) {
	}
}
