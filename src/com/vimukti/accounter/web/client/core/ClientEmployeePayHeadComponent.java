package com.vimukti.accounter.web.client.core;

public class ClientEmployeePayHeadComponent implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientEmployeePayHeadComponent() {

	}

	private ClientPayHead payHead;

	private double rate;

	private long id;

	private String employee;

	private long empPaymentDetails;

	private int version;

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return employee;
	}

	@Override
	public String getDisplayName() {
		return employee;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.EMPLOYEE_PAYHEAD_COMPONENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public ClientPayHead getPayHead() {
		return payHead;
	}

	public void setPayHead(ClientPayHead payHead) {
		this.payHead = payHead;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public long getEmpPaymentDetails() {
		return empPaymentDetails;
	}

	public void setEmpPaymentDetails(long empPaymentDetails) {
		this.empPaymentDetails = empPaymentDetails;
	}
}
