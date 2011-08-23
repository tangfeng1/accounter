package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptException;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class ClientCompany implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	public static final int TYPE_BASIC = 8;

	private Map<String, String> paymentMethods = new HashMap<String, String>();

	int accountingType = 0;

	public long companyID;

	private String fullName;

	private String legalName;

	private String registrationNumber;

	private String companyEmail;

	private String companyEmailForCustomers;

	boolean isConfigured;

	private String ein;

	private int firstMonthOfFiscalYear;

	private int firstMonthOfIncomeTaxYear;

	private int taxForm;

	private long booksClosingDate;

	private int closingDateWarningType;

	private boolean enableAccountNumbers;

	private int customerType;

	private boolean enableAutoRecall;

	boolean restartSetupInterviews;

	private String taxId;

	private int fiscalYearStarting;

	private int industry;

	private long accountsReceivableAccount;

	private long accountsPayableAccount;

	private long openingBalancesAccount;

	private long retainedEarningsAccount;

	private long otherCashIncomeAccount;

	private long otherCashExpenseAccount;

	private long pendingItemReceiptsAccount;
	
	private long cashDiscountsGiven;
	
	private long cashDiscountsTaken;
	
	private long taxLiabilityAccount;
	
	private long VATFiledLiabilityAccount;
	// String prepaidVATaccount;
	// String ECAcquisitionVATaccount;

	private Set<ClientCurrency> currencies;

	String phone;

	String fax;

	String webSite;

	String bankAccountNo;

	String sortCode;

	String serviceItemDefaultIncomeAccount = "Cash Discount Given";

	String serviceItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String nonInventoryItemDefaultIncomeAccount = "Cash Discount Given";

	String nonInventoryItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String ukServiceItemDefaultIncomeAccount = "Early Payment Discount Given";
	String ukServiceItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	String ukNonInventoryItemDefaultIncomeAccount = "Early Payment Discount Given";

	String ukNonInventoryItemDefaultExpenseAccount = "Products/Materials Purchased Type A";

	ClientCompanyPreferences preferences = new ClientCompanyPreferences();

	private VList<ClientAccount> accounts;

	private VList<ClientCustomer> customers;

	private VList<ClientVendor> vendors;

	// private VList<ClientTaxAgency> taxAgencies;

	private VList<ClientItem> items;

	private VList<ClientCustomerGroup> customerGroups;

	private VList<ClientVendorGroup> vendorGroups;

	private VList<ClientPaymentTerms> paymentTerms;

	private VList<ClientShippingTerms> shippingTerms;

	private VList<ClientShippingMethod> shippingMethods;

	private VList<ClientPriceLevel> priceLevels;

	private VList<ClientItemGroup> ItemGroups;

	private VList<ClientTAXGroup> taxGroups;

	private VList<ClientPaySalesTax> paySalesTaxs;

	private VList<ClientCreditRating> creditRatings;

	private VList<ClientSalesPerson> salesPersons;

	// private VList<ClientTaxCode> taxCodes;

	private VList<ClientTAXItemGroup> taxItemGroups;

	private VList<ClientPayee> payees;

	private VList<ClientFiscalYear> fiscalYears;
	private VList<ClientBank> banks;

	private VList<ClientFixedAsset> fixedAssets;

	// private VList<ClientSellingOrDisposingFixedAsset> sellingDisposedItems;

	private VList<ClientVATReturn> vatReturns;

	private VList<ClientTAXAgency> taxAgencies;

	private VList<ClientTAXCode> taxCodes;

	private VList<ClientBrandingTheme> brandingTheme;

	private VList<ClientUserInfo> usersList;

	// private VList<ClientTAXItemGroup> vatItemGroups;

	Set<ClientNominalCodeRange> nominalCodeRange = new HashSet<ClientNominalCodeRange>();

	private ClientFinanceDate transactionStartDate;

	private ClientFinanceDate transactionEndDate;

	private ClientAddress tradingAddress;
	private ClientAddress registeredAddress;

	public void setTaxItemGroups(VList<ClientTAXItemGroup> taxItemGroups) {
		this.taxItemGroups = taxItemGroups;
	}

	// public void setVatItemGroups(VList<ClientTAXItemGroup> vatItemGroups) {
	// this.vatItemGroups = vatItemGroups;
	// }
	//
	// public VList<ClientTAXItemGroup> getVatItemGroups() {
	// return vatItemGroups;
	// }

	public void setTaxCodes(VList<ClientTAXCode> TaxCodes) {
		this.taxCodes = TaxCodes;
	}

	public VList<ClientTAXCode> getTaxCodes() {
		return taxCodes;
	}

	public VList<ClientTAXCode> getActiveTaxCodes() {
		VList<ClientTAXCode> activeTaxCodes = new VList<ClientTAXCode>();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.isActive())
				activeTaxCodes.add(taxCode);
		}
		return activeTaxCodes;
	}

	public void setTaxAgencies(VList<ClientTAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public VList<ClientTAXAgency> getTaxAgencies() {
		return taxAgencies;
	}

	public VList<ClientTAXAgency> getActiveTAXAgencies() {
		return taxAgencies.filter(new ListFilter<ClientTAXAgency>() {

			@Override
			public boolean filter(ClientTAXAgency e) {
				return e.isActive();
			}
		});
		// List<ClientTAXAgency> activeTaxAgencies = new
		// ArrayList<ClientTAXAgency>();
		// for (ClientTAXAgency taxAgency : taxAgencies) {
		// if (taxAgency.isActive())
		// activeTaxAgencies.add(taxAgency);
		// }
		// return activeTaxAgencies;
	}

	/**
	 * @return the vatReturns
	 */
	public VList<ClientVATReturn> getVatReturns() {
		return vatReturns;
	}

	/**
	 * @param vatReturns
	 *            the vatReturns to set
	 */
	public void setVatReturns(VList<ClientVATReturn> vatReturns) {
		this.vatReturns = vatReturns;
	}

	/**
	 * @return the vatReturns
	 */
	public ClientVATReturn getVatReturn(long vatReturnID) {
		return Utility.getObject(this.vatReturns, vatReturnID);
	}

	/**
	 * @return the vatAdjustments
	 */
	public VList<ClientTAXAdjustment> getTaxAdjustments() {
		return taxAdjustments;
	}

	/**
	 * @param taxAdjustments
	 *            the vatAdjustments to set
	 */
	public void setTaxAdjustments(VList<ClientTAXAdjustment> taxAdjustments) {
		this.taxAdjustments = taxAdjustments;
	}

	/**
	 * @return the vatGroups
	 */
	public VList<ClientTAXGroup> getVatGroups() {
		return vatGroups;
	}

	/**
	 * @param vatGroups
	 *            the vatGroups to set
	 */
	public void setVatGroups(VList<ClientTAXGroup> vatGroups) {
		this.vatGroups = vatGroups;
	}

	// /**
	// * @return the vatItems
	// */
	// public VList<ClientTAXItem> getVatItems() {
	// return vatItems;
	// }

	public Set<ClientNominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	public long getPendingItemReceiptsAccount() {
		return pendingItemReceiptsAccount;
	}

	public void setPendingItemReceiptsAccount(long pendingItemReceiptsAccount) {
		this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
	}

	// public VList<ClientTAXItem> getActiveVatItems() {
	// List<ClientTAXItem> activeVatItems = new ArrayList<ClientTAXItem>();
	// for (ClientTAXItem vatitem : vatItems) {
	// if (vatitem.isActive())
	// activeVatItems.add(vatitem);
	// }
	// return activeVatItems);
	// }

	public void setNominalCodeRange(Set<ClientNominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	// /**
	// *
	// * @param vatAgency
	// * @return
	// */
	// public VList<ClientTAXItem> getVatItems(ClientTAXAgency taxAgency) {
	//
	// List<ClientTAXItem> vatItems = new ArrayList<ClientTAXItem>();
	//
	// if (this.vatItems == null) {
	// return vatItems;
	// }
	//
	// if (taxAgency == null) {
	// return vatItems;
	// }
	//
	// for (ClientTAXItem clientVATItem : getVatItems()) {
	//
	// if (clientVATItem.getTaxAgency().equals(taxAgency.getID())) {
	// vatItems.add(clientVATItem);
	// }
	// }
	//
	// return vatItems;
	// }

	// /**
	// * @param vatItems
	// * the vatItems to set
	// */
	// public void setVatItems(VList<ClientTAXItem> vatItems) {
	// this.vatItems = vatItems;
	// }

	public VList<ClientTAXItem> getActiveTaxItems() {
		return taxItems.filter(new ListFilter<ClientTAXItem>() {

			@Override
			public boolean filter(ClientTAXItem e) {
				return e.isActive();
			}
		});
		// List<ClientTAXItem> activeTaxItems = new ArrayList<ClientTAXItem>();
		// for (ClientTAXItem taxItem : taxItems) {
		// if (taxItem.isActive())
		// activeTaxItems.add(taxItem);
		// }
		// return activeTaxItems);
	}

	public VList<ClientTAXItem> getTaxItems() {
		return taxItems;
	}

	public VList<ClientTAXItem> getTaxItems(ClientTAXAgency taxAgency) {
		VList<ClientTAXItem> taxItems = new VList<ClientTAXItem>();

		if (this.taxItems == null) {
			return taxItems;
		}
		if (taxAgency == null) {
			return taxItems;
		}
		for (ClientTAXItem clientTaxItem : getTaxItems()) {
			if (clientTaxItem.getTaxAgency() == taxAgency.getID()) {
				taxItems.add(clientTaxItem);

			}
		}
		return taxItems;

	}

	// public VList<ClientTaxItem> getTaxItems(ClientTaxAgency taxAgency) {
	// List<ClientTaxItem> taxItems = new ArrayList<ClientTaxItem>();
	//
	// if (this.taxItems == null) {
	// return taxItems;
	// }
	// if (taxAgency == null) {
	// return taxItems;
	// }
	// for (ClientTaxItem clientTaxItem : getTaxItems()) {
	// if (clientTaxItem.getTaxAgency().equals(taxAgency.getID())) {
	// taxItems.add(clientTaxItem);
	//
	// }
	// }
	// return taxItems;
	//
	// }

	public void setTaxItems(VList<ClientTAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	private VList<ClientTAXAdjustment> taxAdjustments;

	private VList<ClientTAXGroup> vatGroups;
	private VList<ClientTAXItem> taxItems;

	// private List<ClientTAXItem> vatItems;
	private VList<ClientVATReturnBox> vatReturnBoxes;
	public long id;

	private ClientUser loggedInUser;

	private VList<ClientUnit> units;

	// private List<ClientTaxItem> taxItems;

	public void clientSideInit() {
		paymentMethods.put("1", Accounter.constants().cash());
		paymentMethods.put("2", Accounter.constants().check());
		paymentMethods.put("3", Accounter.constants().creditCard());
	}

	public ClientCompany() {
		// paymentMethods.put("1", "");
		// paymentMethods.put("2", Accounter.constants().check());
		// paymentMethods.put("3", Accounter.constants().creditCard());
	}

	// List<ClientPayType> payTypes;

	public VList<ClientCustomer> getCustomers() {
		return customers;
	}

	public VList<ClientSalesPerson> getsalesPerson() {
		return salesPersons;
	}

	public VList<ClientCustomer> getActiveCustomers() {
		return customers.filter(new ListFilter<ClientCustomer>() {

			@Override
			public boolean filter(ClientCustomer e) {
				return e.isActive();
			}
		});
		// List<ClientCustomer> activeCustomers = new
		// ArrayList<ClientCustomer>();
		// for (ClientCustomer customer : customers) {
		// if (customer.isActive())
		// activeCustomers.add(customer);
		// }
		// return activeCustomers;
	}

	public VList<ClientBank> getBanks() {
		return banks;
	}

	public void setBanks(VList<ClientBank> banks) {
		this.banks = banks;
	}

	public VList<ClientPayee> getPayees() {
		return payees;
	}

	public VList<ClientPayee> getActivePayees() {
		return getPayees().filter(new ListFilter<ClientPayee>() {

			@Override
			public boolean filter(ClientPayee e) {
				return e.isActive();
			}
		});
		// List<ClientPayee> activePayees = new ArrayList<ClientPayee>();
		// for (ClientPayee payee : getPayees()) {
		// if (payee.isActive())
		// activePayees.add(payee);
		// }
		// return activePayees);
	}

	public void setCustomers(VList<ClientCustomer> customers) {
		this.customers = customers;
	}

	public VList<ClientFiscalYear> getFiscalYears() {
		return fiscalYears;
	}

	public void setFiscalYears(VList<ClientFiscalYear> fiscalYears) {
		this.fiscalYears = fiscalYears;
	}

	public VList<ClientVendor> getVendors() {
		return vendors;
	}

	public VList<ClientVendor> getActiveVendors() {
		return vendors.filter(new ListFilter<ClientVendor>() {

			@Override
			public boolean filter(ClientVendor e) {
				return e.isActive();
			}
		});
		// List<ClientVendor> activeVendors = new ArrayList<ClientVendor>();
		// for (ClientVendor vendor : vendors) {
		// if (vendor.isActive())
		// activeVendors.add(vendor);
		// }
		// return activeVendors);
	}

	public void setVendors(VList<ClientVendor> vendors) {
		this.vendors = vendors;
	}

	// public VList<ClientTAXAgency> getTaxAgencies() {
	// return taxAgencies);
	// }
	//
	// public void setTaxAgencies(VList<ClientTAXAgency> taxAgencies) {
	// this.taxAgencies = taxAgencies;
	// }

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public VList<ClientItem> getItems() {
		return items;
	}

	public VList<ClientItem> getServiceItems() {
		return getActiveItems().filter(new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return e.getType() == ClientItem.TYPE_SERVICE;
			}
		});
		// VList<ClientItem> serviceitems = new VList<ClientItem>();
		// for (ClientItem item : getActiveItems()) {
		// if (item.getType() == ClientItem.TYPE_SERVICE)
		// serviceitems.add(item);
		//
		// }
		// return serviceitems;
	}

	public VList<ClientItem> getProductItems() {
		return getActiveItems().filter(new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return e.getType() != ClientItem.TYPE_SERVICE;
			}
		});
		// List<ClientItem> productitems = new ArrayList<ClientItem>();
		// for (ClientItem item : getActiveItems()) {
		// if (item.getType() != ClientItem.TYPE_SERVICE)
		// productitems.add(item);
		//
		// }
		// return productitems);
	}

	public VList<ClientItem> getPurchaseItems() {
		return getAllItems().filter(new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return e.isIBuyThisItem();
			}
		});
		// List<ClientItem> purchaseitems = new ArrayList<ClientItem>();
		// for (ClientItem item : getAllItems()) {
		// if (item.isIBuyThisItem)
		// purchaseitems.add(item);
		//
		// }
		// return purchaseitems);
	}

	public VList<ClientItem> getSalesItems() {
		return getAllItems().filter(new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return e.isISellThisItem();
			}
		});
		// List<ClientItem> salesitems = new ArrayList<ClientItem>();
		// for (ClientItem item : getAllItems()) {
		// if (item.isISellThisItem)
		// salesitems.add(item);
		//
		// }
		// return salesitems);
	}

	public VList<ClientItem> getActiveItems() {
		return items.filter(new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return e.isActive();
			}
		});
		// List<ClientItem> activeItems = new ArrayList<ClientItem>();
		// for (ClientItem item : items) {
		// if (item.isActive())
		// activeItems.add(item);
		// }
		// return activeItems);
	}

	public VList<ClientItem> getAllItems() {
		return items;
		// List<ClientItem> activeItems = new ArrayList<ClientItem>();
		// for (ClientItem item : items) {
		// // if (item.isActive())
		// activeItems.add(item);
		// }
		// return activeItems;
	}

	public void setItems(VList<ClientItem> items) {
		this.items = items;
	}

	public VList<ClientCustomerGroup> getCustomerGroups() {
		return customerGroups;
	}

	public void setCustomerGroups(VList<ClientCustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	public VList<ClientVendorGroup> getVendorGroups() {
		return vendorGroups;
	}

	public void setVendorGroups(VList<ClientVendorGroup> vendorGroups) {
		this.vendorGroups = vendorGroups;
	}

	public VList<ClientPaymentTerms> getPaymentsTerms() {
		return paymentTerms;
	}

	public Map<String, String> getPaymentMethods() {

		return paymentMethods;
	}

	public void setPaymentsTerms(VList<ClientPaymentTerms> paymentsTerms) {
		this.paymentTerms = paymentsTerms;
	}

	public VList<ClientShippingTerms> getShippingTerms() {
		return shippingTerms;
	}

	public void setShippingTerms(VList<ClientShippingTerms> shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public VList<ClientShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(VList<ClientShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public VList<ClientPriceLevel> getPriceLevels() {
		return priceLevels;
	}

	public VList<ClientTAXItemGroup> getTaxItemGroups() {
		return taxItemGroups;
	}

	public void setPriceLevels(VList<ClientPriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}

	public VList<ClientItemGroup> getItemGroups() {
		return ItemGroups;
	}

	public void setItemGroups(VList<ClientItemGroup> itemGroups) {
		ItemGroups = itemGroups;
	}

	// public VList<ClientTaxCode> getTaxcodes() {
	// return taxCodes);
	// }
	// public VList<ClientTaxCode> getActiveTaxCodes() {
	// List<ClientTaxCode> activeTaxCodes = new ArrayList<ClientTaxCode>();
	// for (ClientTaxCode taxCode : taxCodes) {
	// if (taxCode.getIsActive())
	// activeTaxCodes.add(taxCode);
	// }
	// return activeTaxCodes);
	// }
	//
	// public void setTaxcodes(VList<ClientTaxCode> taxcodes) {
	// this.taxCodes = taxcodes;
	// }

	public VList<ClientTAXGroup> getTaxGroups() {
		return taxGroups;
	}

	public void setTaxGroups(VList<ClientTAXGroup> taxGroups) {
		this.taxGroups = taxGroups;
	}

	public VList<ClientTAXAgency> gettaxAgencies() {
		return taxAgencies;
	}

	public VList<ClientTAXAgency> getActiveTaxAgencies() {
		return taxAgencies.filter(new ListFilter<ClientTAXAgency>() {

			@Override
			public boolean filter(ClientTAXAgency e) {
				return e.isActive();
			}
		});
		// List<ClientTAXAgency> activeTaxAgencies = new
		// ArrayList<ClientTAXAgency>();
		// for (ClientTAXAgency taxAgency : taxAgencies) {
		// if (taxAgency.isActive())
		// activeTaxAgencies.add(taxAgency);
		// }
		// return activeTaxAgencies);
	}

	public void settaxAgencies(VList<ClientTAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public VList<ClientPaySalesTax> getPaySalesTaxs() {
		return paySalesTaxs;
	}

	public void setPaySalesTaxs(VList<ClientPaySalesTax> paySalesTaxs) {
		this.paySalesTaxs = paySalesTaxs;
	}

	public VList<ClientCreditRating> getCreditRatings() {
		return creditRatings;
	}

	public void setCreditRatings(VList<ClientCreditRating> creditRatings) {
		this.creditRatings = creditRatings;
	}

	public VList<ClientSalesPerson> getSalesPersons() {
		return salesPersons;
	}

	public VList<ClientSalesPerson> getActiveSalesPersons() {
		return salesPersons.filter(new ListFilter<ClientSalesPerson>() {

			@Override
			public boolean filter(ClientSalesPerson e) {
				return e.isActive();
			}
		});
		// List<ClientSalesPerson> activeSalesPersons = new
		// ArrayList<ClientSalesPerson>();
		// for (ClientSalesPerson salesPerson : salesPersons) {
		// if (salesPerson.isActive())
		// activeSalesPersons.add(salesPerson);
		// }
		// return activeSalesPersons);
	}

	public void setSalesPersons(VList<ClientSalesPerson> salesPersons) {
		this.salesPersons = salesPersons;
	}

	// public void setTaxCodes(VList<ClientTaxCode> taxCodes) {
	// this.taxCodes = taxCodes;
	// }

	public String getCompanyEmailForCustomers() {
		return companyEmailForCustomers;
	}

	public void setCompanyEmailForCustomers(String companyEmailForCustomers) {
		this.companyEmailForCustomers = companyEmailForCustomers;
	}

	// public void setContact(String contact) {
	// this.contact = contact;
	// }

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public int getFirstMonthOfFiscalYear() {
		return firstMonthOfFiscalYear;
	}

	public void setFirstMonthOfFiscalYear(int firstMonthOfFiscalYear) {
		this.firstMonthOfFiscalYear = firstMonthOfFiscalYear;
	}

	public int getFirstMonthOfIncomeTaxYear() {
		return firstMonthOfIncomeTaxYear;
	}

	public void setFirstMonthOfIncomeTaxYear(int firstMonthOfIncomeTaxYear) {
		this.firstMonthOfIncomeTaxYear = firstMonthOfIncomeTaxYear;
	}

	public int getTaxForm() {
		return taxForm;
	}

	public void setTaxForm(int taxForm) {
		this.taxForm = taxForm;
	}

	public long getBooksClosingDate() {
		return booksClosingDate;
	}

	public void setBooksClosingDate(long booksClosingDate) {
		this.booksClosingDate = booksClosingDate;
	}

	public int getClosingDateWarningType() {
		return closingDateWarningType;
	}

	public void setClosingDateWarningType(int closingDateWarningType) {
		this.closingDateWarningType = closingDateWarningType;
	}

	public boolean isEnableAccountNumbers() {
		return enableAccountNumbers;
	}

	public void setEnableAccountNumbers(boolean enableAccountNumbers) {
		this.enableAccountNumbers = enableAccountNumbers;
	}

	public int getCustomerType() {
		return customerType;
	}

	public void setCustomerType(int customerType) {
		this.customerType = customerType;
	}

	public boolean isEnableAutoRecall() {
		return enableAutoRecall;
	}

	public void setEnableAutoRecall(boolean enableAutoRecall) {
		this.enableAutoRecall = enableAutoRecall;
	}

	public boolean isRestartSetupInterviews() {
		return restartSetupInterviews;
	}

	public void setRestartSetupInterviews(boolean restartSetupInterviews) {
		this.restartSetupInterviews = restartSetupInterviews;
	}

	public int getFiscalYearStarting() {
		return fiscalYearStarting;
	}

	public void setFiscalYearStarting(int fiscalYearStarting) {
		this.fiscalYearStarting = fiscalYearStarting;
	}

	public long getAccountsReceivableAccountId() {
		return accountsReceivableAccount;
	}

	public void setAccountsReceivableAccount(long accountsReceivableAccount) {
		this.accountsReceivableAccount = accountsReceivableAccount;
	}

	public long getAccountsPayableAccount() {
		return accountsPayableAccount;
	}

	public void setAccountsPayableAccountId(long accountsPayableAccount) {
		this.accountsPayableAccount = accountsPayableAccount;
	}

	public long getOpeningBalancesAccount() {
		return openingBalancesAccount;
	}

	public void setOpeningBalancesAccountId(long openingBalancesAccount) {
		this.openingBalancesAccount = openingBalancesAccount;
	}

	public long getRetainedEarningsAccount() {
		return retainedEarningsAccount;
	}

	public void setRetainedEarningsAccount(long retainedEarningsAccount) {
		this.retainedEarningsAccount = retainedEarningsAccount;
	}

	public long getOtherCashIncomeAccount() {
		return otherCashIncomeAccount;
	}

	public void setOtherCashIncomeAccount(long otherCashIncomeAccount) {
		this.otherCashIncomeAccount = otherCashIncomeAccount;
	}

	public long getOtherCashExpenseAccount() {
		return otherCashExpenseAccount;
	}

	public void setOtherCashExpenseAccount(long otherCashExpenseAccount) {
		this.otherCashExpenseAccount = otherCashExpenseAccount;
	}

	@Override
	public String getDisplayName() {
		return this.fullName;
	}

	@Override
	public String getName() {

		return this.fullName;
	}

	/**
	 * Called Later at Process Command
	 * 
	 * @param accounts
	 */

	/**
	 * @return the accounts
	 */

	public void setAccounts(VList<ClientAccount> accounts) {
		this.accounts = accounts;
	}

	public VList<ClientAccount> getAccounts() {
		return accounts;
	}

	public VList<ClientAccount> getActiveAccounts() {
		return accounts.filter(new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				return e.getIsActive();
			}
		});
		// List<ClientAccount> activeAccounts = new ArrayList<ClientAccount>();
		// for (ClientAccount account : accounts) {
		// if (account.getIsActive())
		// activeAccounts.add(account);
		// }
		// return activeAccounts);
	}

	public ClientCompanyPreferences getPreferences() {
		return this.preferences;
	}

	public String getTradingName() {
		return this.legalName;
	}

	public String getTaxId() {

		return this.taxId;
	}

	public String getFax() {
		return this.fax;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getWebSite() {
		return this.webSite;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getCompanyEmail() {
		return this.companyEmail;
	}

	public void setName(String stringValue) {
		this.fullName = stringValue;
	}

	public void setTradingName(String stringValue) {
		this.legalName = stringValue;
	}

	public void setPhone(String stringValue) {
		this.phone = stringValue;
	}

	public void setCompanyEmail(String stringValue) {
		this.companyEmail = stringValue;
	}

	public void setTaxId(String stringValue) {
		this.taxId = stringValue;
	}

	public void setFax(String stringValue) {
		this.fax = stringValue;
	}

	public void setWebSite(String stringValue) {
		this.webSite = stringValue;
	}

	public void setIndustry(int typeBasic) {
		this.industry = typeBasic;

	}

	public int getIndustry() {
		return this.industry;
	}

	public ClientPaymentTerms getPaymentTerms(long paymentTermsId) {

		return Utility.getObject(this.paymentTerms, paymentTermsId);
	}

	public ClientPaySalesTax getPaySalesTax(long paysalesTaxId) {

		return Utility.getObject(this.paySalesTaxs, paysalesTaxId);
	}

	public ClientCustomerGroup getCustomerGroup(long customerGroupId) {

		return Utility.getObject(this.customerGroups, customerGroupId);
	}

	public ClientVendorGroup getVendorGroup(long vendorGroupId) {

		return Utility.getObject(this.vendorGroups, vendorGroupId);
	}

	public ClientShippingMethod getShippingMethod(long shippingMethodId) {

		return Utility.getObject(this.shippingMethods, shippingMethodId);
	}

	public ClientShippingTerms getShippingTerms(long shippingTermsId) {

		return Utility.getObject(this.shippingTerms, shippingTermsId);
	}

	public ClientItemGroup getItemGroup(long itemGroupId) {

		return Utility.getObject(this.ItemGroups, itemGroupId);
	}

	public ClientCreditRating getCreditRating(long creditRatingId) {

		return Utility.getObject(this.creditRatings, creditRatingId);
	}

	public ClientTAXAgency getTaxAgency(long taxAgencyId) {

		return Utility.getObject(this.taxAgencies, taxAgencyId);
	}

	// public ClientTaxCode getTaxCode(long taxCodeId) {
	//
	// return Utility.getObject(this.taxCodes, taxCodeId);
	// }

	public ClientCustomer getCustomer(long customerId) {

		return Utility.getObject(this.customers, customerId);
	}

	public ClientCustomer getCustomerByName(String customerName) {
		return Utility.getObjectByName(this.customers, customerName);
	}

	public ClientBank getBank(long bankId) {

		return Utility.getObject(this.banks, bankId);
	}

	public ClientVendor getVendor(long vendorId) {

		return Utility.getObject(this.vendors, vendorId);
	}

	public ClientVendor getVendorByName(String vendorName) {

		return Utility.getObjectByName(this.vendors, vendorName);
	}

	public ClientItem getItem(long itemId) {

		return Utility.getObject(this.items, itemId);
	}

	public ClientAccount getAccount(long accountId) {

		return Utility.getObject(this.accounts, accountId);
	}

	public ClientAccount getAccountByName(String accountName) {

		return Utility.getObjectByName(this.accounts, accountName);
	}

	public ClientTAXGroup getTaxGroup(long taxGroupId) {

		return Utility.getObject(this.taxGroups, taxGroupId);
	}

	public ClientTAXItem getTaxItem(long taxGroupId) {

		return Utility.getObject(this.taxItems, taxGroupId);
	}

	public ClientSalesPerson getSalesPerson(long salesPersonId) {

		return Utility.getObject(this.salesPersons, salesPersonId);
	}

	public ClientFixedAsset getFixedAsset(long fixedAssetID) {

		return Utility.getObject(this.fixedAssets, fixedAssetID);
	}

	public ClientTAXCode getTAXCode(long taxCodeId) {

		return Utility.getObject(this.taxCodes, taxCodeId);
	}

	// public ClientTAXItemGroup getVATItemGroup(long vatItemGrpId) {
	//
	// return Utility.getObject(this.vatItemGroups, vatItemGrpId);
	// }

	public ClientTAXItemGroup getTAXItemGroup(long taxItemGrpId) {

		return Utility.getObject(this.taxItemGroups, taxItemGrpId);
	}

	public ClientPriceLevel getPriceLevel(long priceLevelId) {
		return Utility.getObject(this.priceLevels, priceLevelId);
	}

	public ClientTAXItem getTAXItem(long id) {
		return Utility.getObject(this.taxItems, id);
	}

	// public ClientTAXAgency getTaxAgency(long id) {
	// return Utility.getObject(this.taxAgencies, id);
	// }

	public ClientVATReturnBox getVatReturnBox(long id) {
		return Utility.getObject(this.vatReturnBoxes, id);
	}

	public ClientFiscalYear getFixelYear(long id) {
		return Utility.getObject(this.fiscalYears, id);
	}

	public ClientBrandingTheme getBrandingTheme(long id) {
		return Utility.getObject(this.brandingTheme, id);
	}

	public ClientTAXAgency getVatAgencyByName(String name) {
		return Utility.getObjectByName(this.taxAgencies, name);
	}

	public void deleteSalesPerson(long salesPersonId) {
		this.salesPersons.remove(this.getSalesPerson(salesPersonId));
	}

	public void deletePaymentTerms(long paymentTermsId) {
		this.paymentTerms.remove(this.getPaymentTerms(paymentTermsId));
	}

	public void deletePriceLevel(long priceLevelId) {
		this.priceLevels.remove(this.getPriceLevel(priceLevelId));
	}

	public void deleteCustomerGroup(long customerGroup) {
		this.customerGroups.remove(this.getCustomerGroup(customerGroup));
	}

	public void deleteVendorGroup(long vendorGroup) {
		this.vendorGroups.remove(this.getVendorGroup(vendorGroup));
	}

	public void deleteShippingMethod(long shippingMethod) {
		this.shippingMethods.remove(this.getShippingMethod(shippingMethod));
	}

	public void deleteShippingTerms(long shippingTerm) {
		this.shippingTerms.remove(this.getShippingTerms(shippingTerm));
	}

	public void deleteItemGroup(long itemGroup) {
		this.ItemGroups.remove(this.getItemGroup(itemGroup));
	}

	public void deleteCreditRating(long creditRating) {
		this.creditRatings.remove(this.getCreditRating(creditRating));
	}

	// public void deleteTaxAgency(long taxAgency) {
	// this.taxAgencies.remove(this.getTaxAgency(taxAgency));
	// }

	// public void deleteTaxCode(long taxCode) {
	// this.taxCodes.remove(this.getTaxCode(taxCode));
	// }

	public void deleteCustomer(long customerId) {
		this.customers.remove(this.getCustomer(customerId));
	}

	public void deleteVendor(long vendorId) {
		this.vendors.remove(this.getVendor(vendorId));
	}

	public void deleteItem(long itemId) {
		this.items.remove(this.getItem(itemId));
	}

	public void deleteAccount(long accountId) {
		this.accounts.remove(this.getAccount(accountId));
	}

	public void deleteTaxGroup(long taxGroup) {
		this.taxGroups.remove(this.getTaxGroup(taxGroup));
	}

	public void deleteBank(long bankId) {
		this.banks.remove(this.getBank(bankId));
	}

	public void deleteTaxCode(long taxCode) {
		this.taxCodes.remove(this.getTAXCode(taxCode));
	}

	// public void deleteVatGroup(long vatGroup) {
	// this.vatGroups.remove(this.getVATItem(vatGroup));
	// }

	public void deleteTaxItem(long taxItem) {
		this.taxItems.remove(this.getTAXItem(taxItem));
	}

	public void deleteTaxAgency(long taxAgencyId) {
		this.taxAgencies.remove(this.getTaxAgency(taxAgencyId));
	}

	public void deleteVAtReturn(long vatReturnId) {
		this.vatReturns.remove(this.getVatReturnBox(vatReturnId));
	}

	public void deleteFixelYear(long fixelYearId) {
		this.fiscalYears.remove(this.getFixelYear(fixelYearId));
	}

	public void deleteBrandingTheme(long themeId) {
		this.brandingTheme.remove(this.getBrandingTheme(themeId));
	}

	/**
	 * 
	 * @param accounterCoreObject
	 */

	public void processCommand(Serializable accounterCoreObject) {

		if (accounterCoreObject == null)
			return;

		if (accounterCoreObject instanceof AccounterCommand) {
			AccounterCommand command = (AccounterCommand) accounterCoreObject;
			switch (command.command) {
			case AccounterCommand.CREATION_SUCCESS:
			case AccounterCommand.UPDATION_SUCCESS:
				processUpdateOrCreateObject(command.data);
				break;
			case AccounterCommand.DELETION_SUCCESS:
				processDeleteObject(command.getObjectType(), command.getID());
				return;
			default:
				break;
			}

		}

	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.COMPANY;
	}

	public int getAccountingType() {
		return accountingType;
	}

	public void setAccountingType(int accountingType) {
		this.accountingType = accountingType;
	}

	public String getServiceItemDefaultIncomeAccount() {
		return serviceItemDefaultIncomeAccount;
	}

	public void setServiceItemDefaultIncomeAccount(
			String serviceItemDefaultIncomeAccount) {
		this.serviceItemDefaultIncomeAccount = serviceItemDefaultIncomeAccount;
	}

	public String getServiceItemDefaultExpenseAccount() {
		return serviceItemDefaultExpenseAccount;
	}

	public void setServiceItemDefaultExpenseAccount(
			String serviceItemDefaultExpenseAccount) {
		this.serviceItemDefaultExpenseAccount = serviceItemDefaultExpenseAccount;
	}

	public String getNonInventoryItemDefaultIncomeAccount() {
		return nonInventoryItemDefaultIncomeAccount;
	}

	public void setNonInventoryItemDefaultIncomeAccount(
			String nonInventoryItemDefaultIncomeAccount) {
		this.nonInventoryItemDefaultIncomeAccount = nonInventoryItemDefaultIncomeAccount;
	}

	public String getNonInventoryItemDefaultExpenseAccount() {
		return nonInventoryItemDefaultExpenseAccount;
	}

	public void setNonInventoryItemDefaultExpenseAccount(
			String nonInventoryItemDefaultExpenseAccount) {
		this.nonInventoryItemDefaultExpenseAccount = nonInventoryItemDefaultExpenseAccount;
	}

	public VList<ClientAccount> getAccounts(final int accountType) {
		return getAccounts().filter(new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				if (e != null) {
					return e.type == accountType;
				}
				return false;
			}
		});
		// List<ClientAccount> accounts = new ArrayList<ClientAccount>();
		//
		// for (ClientAccount clientAccount : getAccounts()) {
		// if (clientAccount != null)
		// if (clientAccount.type == accountType) {
		// accounts.add(clientAccount);
		// }
		//
		// }
		//
		// return accounts;
	}

	public VList<ClientAccount> getActiveBankAccounts(final int accountType) {
		return getAccounts().filter(new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				return e.getIsActive() && e.type == accountType;
			}
		});
		// List<ClientAccount> activeAccounts = new ArrayList<ClientAccount>();
		// for (ClientAccount account : getAccounts()) {
		// if (account.getIsActive() && account.type == accountType)
		// activeAccounts.add(account);
		// }
		// return activeAccounts;
	}

	public void processUpdateOrCreateObject(IAccounterCore accounterCoreObject) {

		try {

			// Accounter.showInformation("I came to processUpdateOrCreateObject.......");
			if (accounterCoreObject != null)
				switch (accounterCoreObject.getObjectType()) {

				case ACCOUNT:

					ClientAccount account = (ClientAccount) accounterCoreObject;

					// Utility.updateClientList(account, accounts);

					ClientAccount existObj = Utility.getObject(accounts,
							account.getID());
					if (existObj != null) {
						if (account.getNumber().equals(existObj.getNumber())) {
							accounts.remove(existObj);

							UIUtils.updateAccountsInSortedOrder(accounts,
									account);
						}
						// else {
						// int index = accounts.indexOf(existObj);
						// accounts.remove(existObj);
						// accounts.add(index, account);
						// }
					} else {

						UIUtils.updateAccountsInSortedOrder(accounts, account);
					}

					break;

				case CUSTOMER:

					ClientCustomer customer = (ClientCustomer) accounterCoreObject;

					Utility.updateClientList(customer, customers);
					Utility.updateClientList(customer, payees);

					break;

				case VENDOR:

					ClientVendor vendor = (ClientVendor) accounterCoreObject;

					Utility.updateClientList(vendor, vendors);
					Utility.updateClientList(vendor, payees);

					break;

				case TAXAGENCY:

					ClientTAXAgency taxAgency = (ClientTAXAgency) accounterCoreObject;

					Utility.updateClientList(taxAgency, taxAgencies);
					Utility.updateClientList(taxAgency, payees);

					break;

				case ITEM:

					ClientItem item = (ClientItem) accounterCoreObject;

					Utility.updateClientList(item, items);

					break;

				case TAX_GROUP:

					ClientTAXGroup taxGroup = (ClientTAXGroup) accounterCoreObject;

					// Utility.updateClientList(taxGroup, taxGroups);
					UIUtils.updateClientListAndTaxItemGroup(taxGroup, taxItems,
							taxGroups, taxItemGroups);
					if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
						// ClientTAXCode code =
						// getTAXCodeForTAXItemGroup((ClientTAXItemGroup)
						// taxGroup);
						// Utility.updateClientList(code, taxCodes);
					}

					break;

				case TAX_CODE:

					ClientTAXCode taxCode = (ClientTAXCode) accounterCoreObject;

					Utility.updateClientList(taxCode, taxCodes);

					break;

				case TAXITEM:

					ClientTAXItem taxItem = (ClientTAXItem) accounterCoreObject;

					// Utility.updateClientList(taxItem, taxItems);
					UIUtils.updateClientListAndTaxItemGroup(taxItem, taxItems,
							taxGroups, taxItemGroups);
					if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
						// ClientTAXCode code =
						// getTAXCodeForTAXItemGroup((ClientTAXItemGroup)
						// taxItem);
						// Utility.updateClientList(code, taxCodes);
					}

					break;

				case CUSTOMER_GROUP:

					ClientCustomerGroup customerGroup = (ClientCustomerGroup) accounterCoreObject;

					Utility.updateClientList(customerGroup, customerGroups);

					break;

				case VENDOR_GROUP:

					ClientVendorGroup vendorGroup = (ClientVendorGroup) accounterCoreObject;

					Utility.updateClientList(vendorGroup, vendorGroups);

					break;

				case PAYMENT_TERM:

					ClientPaymentTerms paymentsTerms = (ClientPaymentTerms) accounterCoreObject;

					Utility.updateClientList(paymentsTerms, paymentTerms);

					break;

				case SHIPPING_METHOD:

					ClientShippingMethod shippingMethod = (ClientShippingMethod) accounterCoreObject;

					Utility.updateClientList(shippingMethod, shippingMethods);

					break;

				case SHIPPING_TERM:

					ClientShippingTerms shippingTerm = (ClientShippingTerms) accounterCoreObject;

					Utility.updateClientList(shippingTerm, shippingTerms);

					break;

				case PRICE_LEVEL:

					ClientPriceLevel priceLevel = (ClientPriceLevel) accounterCoreObject;

					Utility.updateClientList(priceLevel, priceLevels);

					break;

				case ITEM_GROUP:

					ClientItemGroup itemGroup = (ClientItemGroup) accounterCoreObject;

					Utility.updateClientList(itemGroup, ItemGroups);

					break;

				case SALES_PERSON:

					ClientSalesPerson salesPerson = (ClientSalesPerson) accounterCoreObject;

					Utility.updateClientList(salesPerson, salesPersons);

					break;

				case CREDIT_RATING:

					ClientCreditRating creditRating = (ClientCreditRating) accounterCoreObject;

					Utility.updateClientList(creditRating, creditRatings);

					break;

				case PAY_SALES_TAX:

					ClientPaySalesTax paySalesTax = (ClientPaySalesTax) accounterCoreObject;

					Utility.updateClientList(paySalesTax, paySalesTaxs);

					break;

				case BANK:

					ClientBank clientBank = (ClientBank) accounterCoreObject;
					Utility.updateClientList(clientBank, banks);
					break;

				case FIXEDASSET:
					ClientFixedAsset fixedAsset = (ClientFixedAsset) accounterCoreObject;
					Utility.updateClientList(fixedAsset, fixedAssets);
					break;

				// case VATITEM:
				// ClientTAXItem vatItem = (ClientTAXItem) accounterCoreObject;
				// Utility.updateClientList(vatItem, this.vatItems);
				// ViewManager.updateComboDataInViews(SelectItemType.VAT_ITEM,
				// accounterCoreObject);
				// break;
				// case VATGROUP:
				// ClientTAXGroup vatGroup = (ClientTAXGroup)
				// accounterCoreObject;
				// Utility.updateClientList(vatGroup, this.vatGroups);
				//
				// break;
				// case VATCODE:
				// ClientVATCode code = (ClientVATCode) accounterCoreObject;
				// Utility.updateClientList(code, this.vatCodes);
				// break;
				// case VATAGENCY:
				// ClientVATAgency vagy = (ClientVATAgency) accounterCoreObject;
				// Utility.updateClientList(vagy, this.vatAgencies);
				// Utility.updateClientList(vagy, payees);
				// break;
				case VATRETURN:
					ClientVATReturn vaReturn = (ClientVATReturn) accounterCoreObject;
					Utility.updateClientList(vaReturn, this.vatReturns);
					break;

				case FISCALYEAR:
					ClientFiscalYear fiscalYear = (ClientFiscalYear) accounterCoreObject;
					Utility.updateClientList(fiscalYear, this.fiscalYears);
					sortFiscalYears();
					break;

				case COMPANY_PREFERENCES:
					this.preferences = (ClientCompanyPreferences) accounterCoreObject;
					break;
				case COMPANY:
					ClientCompany cmp = (ClientCompany) accounterCoreObject;
					this.getToClientCompany(cmp);
					break;
				case BRANDINGTHEME:
					ClientBrandingTheme theme = (ClientBrandingTheme) accounterCoreObject;
					Utility.updateClientList(theme, brandingTheme);
					break;
				case USER:
					ClientUserInfo user = (ClientUserInfo) accounterCoreObject;
					Utility.updateClientList(user, this.usersList);
					long userID = user.getID();
					ClientUser loggedInUser = getLoggedInUser();
					if (loggedInUser.getID() == userID) {
						loggedInUser.firstName = user.getFirstName();
						loggedInUser.fullName = user.getFullName();
						loggedInUser.lastName = user.getLastName();
						Accounter.setUser(loggedInUser);
					}
				}
		} catch (Exception e) {
			if (e instanceof JavaScriptException) {
				Accounter.showInformation(Accounter.constants()
						.exceptionOccur()
						+ ((JavaScriptException) (e)).getDescription());

			} else {
				Accounter.showInformation(Accounter.constants()
						.exceptionOccur() + e.toString());
			}
		}

	}

	public void processDeleteObject(AccounterCoreType objectType, long id) {

		switch (objectType) {

		case ACCOUNT:

			deleteAccount(id);

			break;

		case CUSTOMER:
			deleteCustomer(id);
			if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.id == id) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case VENDOR:

			deleteVendor(id);
			if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.id == id) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case TAXAGENCY:

			deleteTaxAgency(id);
			if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientPayee payee : getActivePayees()) {
					if (payee.id == id) {
						Utility.isDelete = true;
						Utility.updateClientList(payee, payees);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case ITEM:

			deleteItem(id);

			break;

		case TAX_GROUP:

			deleteTaxGroup(id);
			if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientTAXCode taxCode : getActiveTaxCodes()) {
					if (taxCode.id == id) {
						Utility.isDelete = true;
						Utility.updateClientList(taxCode, taxCodes);
						Utility.isDelete = false;
					}
				}
			}

			break;

		case TAX_CODE:

			deleteTaxCode(id);

			break;

		case CUSTOMER_GROUP:
			deleteCustomerGroup(id);
			break;

		case VENDOR_GROUP:

			deleteVendorGroup(id);
			break;
		case PAYMENT_TERM:
			deletePaymentTerms(id);
			break;
		case SHIPPING_METHOD:
			deleteShippingMethod(id);
			break;

		case SHIPPING_TERM:
			deleteShippingTerms(id);
			break;

		case PRICE_LEVEL:
			deletePriceLevel(id);
			break;

		case ITEM_GROUP:
			deleteItemGroup(id);
			break;

		case SALES_PERSON:
			deleteSalesPerson(id);
			break;

		case CREDIT_RATING:

			deleteCreditRating(id);
			break;

		case BANK:
			deleteBank(id);
			break;

		case FIXEDASSET:

			deleteFixedAsset(id);
			break;
		case TAXITEM:
			deleteTaxItem(id);
			if (getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				for (ClientTAXCode taxCode : getActiveTaxCodes()) {
					if (taxCode.id == id) {
						Utility.isDelete = true;
						Utility.updateClientList(taxCode, taxCodes);
						Utility.isDelete = false;
					}
				}
			}

			break;
		// case VATGROUP:
		// deleteVatGroup(id);
		// break;
		// case VATCODE:
		// deleteVatCode(id);
		// break;
		// case VATAGENCY:
		// deleteVatAgency(id);
		// break;
		case VATRETURN:
			deleteVAtReturn(id);
			break;
		case FISCALYEAR:
			deleteFixelYear(id);
			break;
		case BRANDINGTHEME:
			deleteBrandingTheme(id);
			break;
		}
	}

	// private void deleteSoldDisposedAsset(String id) {
	// this.sellingDisposedItems.remove(this.getSellingDisposedItem(id));
	//
	// }

	public void deleteFixedAsset(long id) {
		this.fixedAssets.remove(this.getFixedAsset(id));
	}

	@Override
	public long getID() {
		return this.companyID;
	}

	@Override
	public void setID(long id) {
		this.companyID = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientCompany";
	}

	public void setFixedAssets(VList<ClientFixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public VList<ClientFixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	// public VList<ClientSellingOrDisposingFixedAsset>
	// getSellingDisposedItems()
	// {
	// return sellingDisposedItems;
	// }
	//
	// public void setSellingDisposedItems(
	// List<ClientSellingOrDisposingFixedAsset> assets) {
	// this.sellingDisposedItems = assets;
	// }
	//
	// public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
	// String assetID) {
	// return Utility.getObject(this.sellingDisposedItems, assetID);
	// }

	public boolean objectExists(IAccounterCore core) {

		if (core == null || core.getObjectType() == null) {
			return false;
		}

		AccounterCoreType type = core.getObjectType();

		boolean alreadyExists = false;

		switch (type) {
		case ACCOUNT:
			alreadyExists = Utility.getObjectByName(accounts, core.getName()) != null;
			break;

		case CUSTOMER:
			alreadyExists = Utility.getObjectByName(customers, core.getName()) != null;
			break;

		case VENDOR:
			alreadyExists = Utility.getObjectByName(vendors, core.getName()) != null;
			break;

		case TAX_CODE:
			break;

		default:
			break;
		}

		return alreadyExists;
	}

	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (ClientNominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	public void setVatReturnBoxes(VList<ClientVATReturnBox> vatReturnBoxes) {
		this.vatReturnBoxes = vatReturnBoxes;
	}

	public VList<ClientVATReturnBox> getVatReturnBoxes() {
		return vatReturnBoxes;
	}

	public ClientVATReturnBox getVatReturnBoxByID(long vatReturnBoxID) {
		return Utility.getObject(this.vatReturnBoxes, vatReturnBoxID);
	}

	// public ClientTAXItem getVATItemByName(String name) {
	// return Utility.getObjectByName(this.vatItems, name);
	// }

	public void setUkServiceItemDefaultIncomeAccount(
			String ukServiceItemDefaultIncomeAccount) {
		this.ukServiceItemDefaultIncomeAccount = ukServiceItemDefaultIncomeAccount;
	}

	public String getUkServiceItemDefaultIncomeAccount() {
		return ukServiceItemDefaultIncomeAccount;
	}

	public void setUkServiceItemDefaultExpenseAccount(
			String ukServiceItemDefaultExpenseAccount) {
		this.ukServiceItemDefaultExpenseAccount = ukServiceItemDefaultExpenseAccount;
	}

	public String getUkServiceItemDefaultExpenseAccount() {
		return ukServiceItemDefaultExpenseAccount;
	}

	public void setUkNonInventoryItemDefaultIncomeAccount(
			String ukNonInventoryItemDefaultIncomeAccount) {
		this.ukNonInventoryItemDefaultIncomeAccount = ukNonInventoryItemDefaultIncomeAccount;
	}

	public String getUkNonInventoryItemDefaultIncomeAccount() {
		return ukNonInventoryItemDefaultIncomeAccount;
	}

	public void setUkNonInventoryItemDefaultExpenseAccount(
			String ukNonInventoryItemDefaultExpenseAccount) {
		this.ukNonInventoryItemDefaultExpenseAccount = ukNonInventoryItemDefaultExpenseAccount;
	}

	public String getUkNonInventoryItemDefaultExpenseAccount() {
		return ukNonInventoryItemDefaultExpenseAccount;
	}

	public boolean isUKAccounting() {

		return this.accountingType == ACCOUNTING_TYPE_UK;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void getToClientCompany(ClientCompany clientCompany) {

		this.fullName = clientCompany.fullName;
		this.phone = clientCompany.phone;
		this.companyEmail = clientCompany.companyEmail;
		this.legalName = clientCompany.legalName;
		this.fax = clientCompany.fax;
		this.webSite = clientCompany.webSite;
		this.registrationNumber = clientCompany.registrationNumber;
		this.taxId = clientCompany.taxId;
		this.bankAccountNo = clientCompany.bankAccountNo;
		this.sortCode = clientCompany.sortCode;
		this.preferences = clientCompany.preferences;

	}

	public ClientTAXCode getTAXCodeForTAXItemGroup(
			ClientTAXItemGroup taxItemGroup) {
		boolean exist = false;
		ClientTAXCode taxCode = new ClientTAXCode();
		taxCode.setID(taxItemGroup.getID());
		taxCode.setName(taxItemGroup.getName());
		taxCode.setDescription(taxItemGroup.description);
		taxCode.setActive(taxItemGroup.isActive());
		taxCode.setTAXItemGrpForSales(taxItemGroup.getID());
		taxCode.setTaxable(true);
		taxCode.setECSalesEntry(false);
		taxCode.setTAXItemGrpForPurchases(0);
		for (ClientTAXCode tempCode : taxCodes) {
			if (tempCode.getName().equalsIgnoreCase(taxCode.getName())) {
				exist = true;
				break;
			}
		}
		if (!exist)
			Utility.updateClientList(taxCode, taxCodes);
		return taxCode;
	}

	public void setBrandingThemes(VList<ClientBrandingTheme> brandingTheme) {
		this.brandingTheme = brandingTheme;
	}

	public VList<ClientBrandingTheme> getBrandingTheme() {
		return brandingTheme;
	}

	public void setUsersList(VList<ClientUserInfo> users) {
		this.usersList = users;
	}

	public VList<ClientUserInfo> getUsersList() {
		return usersList;
	}

	public ClientAccount getAccountByNumber(long accountNo) {
		for (ClientAccount account : getAccounts()) {
			if (account.getNumber().equals(String.valueOf(accountNo)))
				return account;
		}

		return null;

	}

	public ClientFinanceDate getLastandOpenedFiscalYearStartDate() {
		List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
		if (!clientFiscalYears.isEmpty())
			return clientFiscalYears.get((clientFiscalYears.size() - 1))
					.getStartDate();
		return null;
	}

	public ClientFinanceDate getCurrentFiscalYearStartDate() {
		List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
		boolean isCurrentOne = false;
		for (int i = clientFiscalYears.size() - 1; i >= 0; i--) {
			isCurrentOne = new ClientFinanceDate().before(clientFiscalYears
					.get(i).getEndDate())
					&& new ClientFinanceDate().after(clientFiscalYears.get(i)
							.getStartDate());
			if (isCurrentOne)
				return clientFiscalYears.get(i).getStartDate();
		}
		if (!isCurrentOne) {
			return getLastandOpenedFiscalYearStartDate();
		}
		return null;
	}

	public ClientFinanceDate getCurrentFiscalYearEndDate() {
		List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
		boolean isCurrentOne = false;
		for (int i = clientFiscalYears.size() - 1; i >= 0; i--) {
			isCurrentOne = new ClientFinanceDate().before(clientFiscalYears
					.get(i).getEndDate())
					&& new ClientFinanceDate().after(clientFiscalYears.get(i)
							.getStartDate());
			if (isCurrentOne)
				return clientFiscalYears.get(i).getEndDate();
		}
		if (!isCurrentOne) {
			return getLastandOpenedFiscalYearEndDate();
		}
		return null;
	}

	public ClientFinanceDate getLastandOpenedFiscalYearEndDate() {
		List<ClientFiscalYear> clientFiscalYears = getFiscalYears();

		if (!clientFiscalYears.isEmpty())
			return clientFiscalYears.get((clientFiscalYears.size() - 1))
					.getEndDate();

		return null;
	}

	public boolean isCurrentInFiscalYear(long date) {
		List<ClientFiscalYear> clientFiscalYears = getFiscalYears();
		boolean isCurrentOne = false;
		for (int i = clientFiscalYears.size() - 1; i >= 0; i--) {
			long startDate = Math.round(clientFiscalYears.get(i).getStartDate()
					.getDate() / 100);
			long endDate = Math.round(clientFiscalYears.get(i).getEndDate()
					.getDate() / 100);
			isCurrentOne = (date >= startDate) && (date <= endDate);
			if (isCurrentOne)
				return true;
		}
		return false;
	}

	public void sortFiscalYears() {
		Collections.sort(getFiscalYears(), new Comparator<ClientFiscalYear>() {

			@Override
			public int compare(ClientFiscalYear o1, ClientFiscalYear o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		});
	}

	/**
	 * @return the transactionStartDate
	 */
	public ClientFinanceDate getTransactionStartDate() {
		return transactionStartDate;
	}

	/**
	 * @param transactionStartDate
	 *            the transactionStartDate to set
	 */
	public void setTransactionStartDate(ClientFinanceDate transactionStartDate) {
		this.transactionStartDate = transactionStartDate;
	}

	/**
	 * @return the transactionEndDate
	 */
	public ClientFinanceDate getTransactionEndDate() {
		return transactionEndDate;
	}

	/**
	 * @param transactionEndDate
	 *            the transactionEndDate to set
	 */
	public void setTransactionEndDate(ClientFinanceDate transactionEndDate) {
		this.transactionEndDate = transactionEndDate;
	}

	/**
	 * @return the tradingAddress
	 */
	public ClientAddress getTradingAddress() {
		return tradingAddress;
	}

	/**
	 * @param tradingAddress
	 *            the tradingAddress to set
	 */
	public void setTradingAddress(ClientAddress tradingAddress) {
		this.tradingAddress = tradingAddress;
	}

	/**
	 * @return the registeredAddress
	 */
	public ClientAddress getRegisteredAddress() {
		return registeredAddress;
	}

	/**
	 * @param registeredAddress
	 *            the registeredAddress to set
	 */
	public void setRegisteredAddress(ClientAddress registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	/**
	 * @return
	 */
	public ClientUser getLoggedInUser() {
		return this.loggedInUser;
	}

	public void setLoggedInUser(ClientUser user) {
		this.loggedInUser = user;
	}

	public void setCurrencies(Set<ClientCurrency> currencies) {
		this.currencies = currencies;
	}

	public Set<ClientCurrency> getCurrencies() {
		return currencies;
	}

	/**
	 * @param string
	 */
	public ClientCurrency getCurrency(String code) {
		for (ClientCurrency currency : currencies) {
			if (currency.getSymbol().equals(code)) {
				return currency;
			}
		}
		return null;
	}

	public ClientCompany clone() {
		ClientCompany clientCompany = (ClientCompany) this.clone();
		VList<ClientAccount> accounts = new VList<ClientAccount>();
		for (ClientAccount clientAccount : this.accounts) {
			accounts.add(clientAccount.clone());
		}
		clientCompany.accounts = accounts;

		VList<ClientBank> banks = new VList<ClientBank>();
		for (ClientBank clientBank : this.banks) {
			banks.add(clientBank.clone());
		}
		clientCompany.banks = banks;

		VList<ClientBrandingTheme> brandingThemes = new VList<ClientBrandingTheme>();
		for (ClientBrandingTheme clientBrandingTheme : this.brandingTheme) {
			brandingThemes.add(clientBrandingTheme.clone());
		}
		clientCompany.brandingTheme = brandingTheme;

		VList<ClientCreditRating> creditRatings = new VList<ClientCreditRating>();
		for (ClientCreditRating clientCreditRating : this.creditRatings) {
			creditRatings.add(clientCreditRating.clone());
		}
		clientCompany.creditRatings = creditRatings;

		Set<ClientCurrency> currencies = new HashSet<ClientCurrency>();
		for (ClientCurrency clientCurrency : this.currencies) {
			currencies.add(clientCurrency.clone());
		}
		clientCompany.currencies = currencies;

		VList<ClientCustomerGroup> customerGroups = new VList<ClientCustomerGroup>();
		for (ClientCustomerGroup clientCustomerGroup : this.customerGroups) {
			customerGroups.add(clientCustomerGroup.clone());
		}
		clientCompany.customerGroups = customerGroups;

		VList<ClientCustomer> customers = new VList<ClientCustomer>();
		for (ClientCustomer clientCustomer : this.customers) {
			customers.add(clientCustomer.clone());
		}
		clientCompany.customers = customers;

		VList<ClientFiscalYear> fiscalYears = new VList<ClientFiscalYear>();
		for (ClientFiscalYear clientFiscalYear : this.fiscalYears) {
			fiscalYears.add(clientFiscalYear.clone());
		}
		clientCompany.fiscalYears = fiscalYears;

		VList<ClientFixedAsset> fixedAssets = new VList<ClientFixedAsset>();
		for (ClientFixedAsset clientfixedAsset : this.fixedAssets) {
			fixedAssets.add(clientfixedAsset.clone());
		}
		clientCompany.fixedAssets = fixedAssets;

		VList<ClientItemGroup> itemGroups = new VList<ClientItemGroup>();
		for (ClientItemGroup clientItemGroup : this.ItemGroups) {
			itemGroups.add(clientItemGroup.clone());
		}
		clientCompany.ItemGroups = itemGroups;

		VList<ClientItem> items = new VList<ClientItem>();
		for (ClientItem clientItem : this.items) {
			items.add(clientItem.clone());
		}
		clientCompany.items = items;

		Set<ClientNominalCodeRange> nominalCodeRanges = new HashSet<ClientNominalCodeRange>();
		for (ClientNominalCodeRange clientNominalCodeRange : this.nominalCodeRange) {
			nominalCodeRanges.add(clientNominalCodeRange.clone());
		}
		clientCompany.nominalCodeRange = nominalCodeRanges;

		VList<ClientPayee> payees = new VList<ClientPayee>();
		for (ClientPayee clientPayee : this.payees) {
			payees.add(clientPayee.clone());
		}
		clientCompany.payees = payees;

		// TODO clientCompany.paymentMethods

		VList<ClientPaymentTerms> paymentTerms = new VList<ClientPaymentTerms>();
		for (ClientPaymentTerms clientPaymentTerms : this.paymentTerms) {
			paymentTerms.add(clientPaymentTerms.clone());
		}
		clientCompany.paymentTerms = paymentTerms;

		VList<ClientPaySalesTax> paySalesTaxs = new VList<ClientPaySalesTax>();
		for (ClientPaySalesTax clientPaySalesTax : this.paySalesTaxs) {
			paySalesTaxs.add(clientPaySalesTax.clone());
		}
		clientCompany.paySalesTaxs = paySalesTaxs;

		VList<ClientPriceLevel> priceLevels = new VList<ClientPriceLevel>();
		for (ClientPriceLevel clientPriceLevel : this.priceLevels) {
			priceLevels.add(clientPriceLevel.clone());
		}
		clientCompany.priceLevels = priceLevels;

		VList<ClientSalesPerson> salesPersons = new VList<ClientSalesPerson>();
		for (ClientSalesPerson clientSalesPerson : this.salesPersons) {
			salesPersons.add(clientSalesPerson.clone());
		}
		clientCompany.salesPersons = salesPersons;

		VList<ClientShippingMethod> shippingMethods = new VList<ClientShippingMethod>();
		for (ClientShippingMethod clientShippingMethod : this.shippingMethods) {
			shippingMethods.add(clientShippingMethod.clone());
		}
		clientCompany.shippingMethods = shippingMethods;

		VList<ClientShippingTerms> shippingTerms = new VList<ClientShippingTerms>();
		for (ClientShippingTerms clientShippingTerms : this.shippingTerms) {
			shippingTerms.add(clientShippingTerms.clone());
		}
		clientCompany.shippingTerms = shippingTerms;

		VList<ClientTAXAdjustment> taxAdjustments = new VList<ClientTAXAdjustment>();
		for (ClientTAXAdjustment clientTAXAdjustment : this.taxAdjustments) {
			taxAdjustments.add(clientTAXAdjustment.clone());
		}
		clientCompany.taxAdjustments = taxAdjustments;

		VList<ClientTAXAgency> taxAgencies = new VList<ClientTAXAgency>();
		for (ClientTAXAgency clientTAXAgency : this.taxAgencies) {
			taxAgencies.add(clientTAXAgency.clone());
		}
		clientCompany.taxAgencies = taxAgencies;

		VList<ClientTAXCode> taxCodes = new VList<ClientTAXCode>();
		for (ClientTAXCode clientTAXCode : this.taxCodes) {
			taxCodes.add(clientTAXCode.clone());
		}
		clientCompany.taxCodes = taxCodes;

		VList<ClientTAXGroup> taxGroups = new VList<ClientTAXGroup>();
		for (ClientTAXGroup clientTAXGroup : this.taxGroups) {
			taxGroups.add(clientTAXGroup.clone());
		}
		clientCompany.taxGroups = taxGroups;

		VList<ClientTAXItem> taxItems = new VList<ClientTAXItem>();
		for (ClientTAXItem clientTAXItem : this.taxItems) {
			taxItems.add(clientTAXItem.clone());
		}
		clientCompany.taxItems = taxItems;

		VList<ClientUserInfo> usersList = new VList<ClientUserInfo>();
		for (ClientUserInfo clientEmployee : this.usersList) {
			usersList.add(clientEmployee.clone());
		}
		clientCompany.usersList = usersList;

		VList<ClientTAXGroup> vatGroups = new VList<ClientTAXGroup>();
		for (ClientTAXGroup clientTAXGroup : this.vatGroups) {
			vatGroups.add(clientTAXGroup.clone());
		}
		clientCompany.vatGroups = vatGroups;

		VList<ClientVendor> vendors = new VList<ClientVendor>();
		for (ClientVendor clientVendor : this.vendors) {
			vendors.add(clientVendor.clone());
		}
		clientCompany.vendors = vendors;

		clientCompany.loggedInUser = this.loggedInUser.clone();
		clientCompany.preferences = this.preferences.clone();
		clientCompany.registeredAddress = this.registeredAddress.clone();
		clientCompany.tradingAddress = this.tradingAddress.clone();
		clientCompany.transactionEndDate = this.transactionEndDate.clone();
		clientCompany.transactionStartDate = this.transactionStartDate.clone();

		return clientCompany;

	}

	/**
	 * @param unit
	 * @return
	 */
	public ClientUnit getUnitById(long unitID) {
		for (ClientUnit u : units) {
			if (u.getId() == unitID) {
				return u;
			}
		}
		return null;
	}

	/**
	 * @param units
	 */
	public void setUnits(VList<ClientUnit> units) {
		this.units = units;
	}

	/**
	 * @return
	 */
	public VList<ClientUnit> getUnits() {
		return units;
	}

	public ClientTAXCode getTaxCodeByName(String name) {
		return Utility.getObjectByName(getTaxCodes(), name);
	}

	public ClientItemGroup getItemGroupByName(String name) {
		return Utility.getObjectByName(getItemGroups(), name);
	}

	public ClientCreditRating getCreditRatingByName(String name) {
		return Utility.getObjectByName(getCreditRatings(), name);
	}

	public ClientItem getItemByName(String name) {
		return Utility.getObjectByName(getItems(), name);
	}

	public ClientPaymentTerms getPaymentTermsByName(String name) {
		return Utility.getObjectByName(getPaymentsTerms(), name);
	}

	public ClientPriceLevel getPriceLevelByName(String name) {
		return Utility.getObjectByName(getPriceLevels(), name);
	}

	public ClientTAXGroup getTaxGroupByName(String name) {
		return Utility.getObjectByName(getTaxGroups(), name);
	}

	public ClientShippingMethod getShippingMethodByName(String name) {
		return Utility.getObjectByName(getShippingMethods(), name);
	}

	public ClientVendorGroup getVendorGroupByName(String name) {
		return Utility.getObjectByName(getVendorGroups(), name);
	}

	public ClientCustomerGroup getCustomerGroupByName(String name) {
		return Utility.getObjectByName(getCustomerGroups(), name);
	}

	public ClientFixedAsset getFixedAssetByName(String name) {
		return Utility.getObjectByName(getFixedAssets(), name);
	}

	public ClientBrandingTheme getBrandingThemeByName(String name) {
		return Utility.getObjectByName(getBrandingTheme(), name);
	}

	public ClientTAXItem getTaxItemByName(String name) {
		return Utility.getObjectByName(getTaxItems(), name);
	}

	public ClientSalesPerson getSalesPersonByName(String name) {
		return Utility.getObjectByName(getsalesPerson(), name);
	}

	public ClientVendorGroup getVendorGroupsByName(String name) {
		return Utility.getObjectByName(getVendorGroups(), name);
	}

	public ClientTAXGroup getVatGroupsbyname(String name) {
		return Utility.getObjectByName(getVatGroups(), name);
	}

	public ClientTAXAgency getTaxAgenciesByName(String name) {
		return Utility.getObjectByName(gettaxAgencies(), name);
	}

	public ClientTAXCode getTAXCodeByName(String name) {
		return Utility.getObjectByName(getTaxCodes(), name);
	}

	public ClientUserInfo getUserById(long id) {
		for (ClientUserInfo employee : usersList) {
			if (employee.getID() == id) {
				return employee;
			}
		}
		return null;
	}

	public long getCashDiscountsGiven() {
		return cashDiscountsGiven;
	}

	public void setCashDiscountsGiven(long cashDiscountsGiven) {
		this.cashDiscountsGiven = cashDiscountsGiven;
	}

	public long getCashDiscountsTaken() {
		return cashDiscountsTaken;
	}

	public void setCashDiscountsTaken(long cashDiscountsTaken) {
		this.cashDiscountsTaken = cashDiscountsTaken;
	}

	public long getTaxLiabilityAccount() {
		return taxLiabilityAccount;
	}

	public void setTaxLiabilityAccount(long taxLiabilityAccount) {
		this.taxLiabilityAccount = taxLiabilityAccount;
	}

	public long getVATFiledLiabilityAccount() {
		return VATFiledLiabilityAccount;
	}

	public void setVATFiledLiabilityAccount(long vATFiledLiabilityAccount) {
		VATFiledLiabilityAccount = vATFiledLiabilityAccount;
	}
}
