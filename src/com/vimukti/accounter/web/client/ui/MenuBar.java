package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.countries.UnitedKingdom;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class MenuBar {

	private final List<Menu> menus;

	private final AccounterMessages messages = Global.get().messages();

	private boolean canDoBanking;

	private boolean canDoInvoiceTransactions;

	private boolean canChangeSettings;

	private boolean isTrackTax;

	private boolean isLocationTracking;

	private boolean isClassTracking;

	private boolean canSeeBanking;

	private boolean canSeeInvoiceTransactions;

	private boolean isDoyouwantEstimates;

	private boolean isDelayedchargesEnabled;

	private boolean isHaveEpmloyees;

	private boolean isTrackEmployeeExpenses;

	private boolean isKeepTrackofBills;

	private boolean canViewReports;

	private boolean iswareHouseEnabled;

	private boolean isPurchaseOrderEnabled;

	private boolean isSalesOrderEnabled;

	private boolean isClassTrackingEnabled;

	private boolean isLocationTrackingEnabled;

	private boolean isTaxTracking;

	private ICountryPreferences company;

	private boolean canManageFiscalYears;

	private boolean isInventoryEnabled;

	public MenuBar() {
		menus = new ArrayList<Menu>();

	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void addMenu(Menu menu) {

		menus.add(menu);
	}

	void getMenuBar() {

		this.addMenu(getCompanyMenu(messages.company()));

		if (isTrackTax) {
			this.addMenu(getVATMenu(messages.tax()));
		}

		this.addMenu(getCustomerMenu(Global.get().Customer()));

		this.addMenu(getVendorMenu(Global.get().Vendor()));

		if (canDoBanking) {
			this.addMenu(getBankingMenu(messages.banking()));
		}

		if (isSalesOrderEnabled) {
			this.addMenu(getSalesSubMenu(messages.sales()));

		}

		if (isPurchaseOrderEnabled) {
			this.addMenu(getPurchaseSubMenu(messages.purchases()));
		}

		if (isInventoryEnabled) {
			this.addMenu(getInventoryMenu(messages.inventory()));
		}

		// this.addMenu(getFixedAssetsMenu(messages.fixedAssets()));

		if (canViewReports) {
			this.addMenu(getReportMenu(messages.reports()));
		}
		if (canChangeSettings) {
			this.addMenu(getSettingsMenu(messages.settings()));
		}

	}

	private Menu getInventoryMenu(String string) {

		Menu inventoryMenuBar = new Menu(string);

		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly())) {
			inventoryMenuBar.addMenuItem(messages.stockAdjustment(),
					HistoryTokens.STOCKADJUSTMENT);
			inventoryMenuBar.addMenuItem(getNewInventoryMenu(messages.new1()));
		}
		inventoryMenuBar.addMenuItem(getInventoryListsMenu(messages
				.InventoryLists()));

		return inventoryMenuBar;
	}

	private Menu getInventoryListsMenu(String string) {
		Menu inventoryMenu = new Menu(string);

		inventoryMenu.addMenuItem(messages.inventoryItems(),
				HistoryTokens.INVENTORYITEM);

		if (iswareHouseEnabled) {
			inventoryMenu.addMenuItem(messages.warehouseList(),
					HistoryTokens.WAREHOUSELIST);
			inventoryMenu.addMenuItem(messages.warehouseTransferList(),
					HistoryTokens.WAREHOUSETRANSFERLIST);
		}
		inventoryMenu.addMenuItem(messages.stockAdjustments(),
				HistoryTokens.STOCKADJUSTMENTS);
		inventoryMenu.addMenuItem(messages.measurement(),
				HistoryTokens.MEASUREMENTLIST);

		return inventoryMenu;
	}

	private Menu getNewInventoryMenu(String string) {

		Menu newMenuBar = new Menu(string);

		if (iswareHouseEnabled) {
			newMenuBar.addMenuItem(messages.wareHouse(),
					HistoryTokens.WAREHOUSE);

			newMenuBar.addMenuItem(messages.wareHouseTransfer(),
					HistoryTokens.WAREHOUSETRANSFER);
		}
		newMenuBar.addMenuItem(messages.addMeasurementName(),
				HistoryTokens.ADDMEASUREMENT);
		return newMenuBar;

	}

	private Menu getSettingsMenu(String string) {

		Menu settingsMenuBar = new Menu(string);

		settingsMenuBar.addMenuItem(messages.generalSettings(),
				HistoryTokens.GENERALSETTINGS);
		settingsMenuBar.addMenuItem(messages.translation(),
				HistoryTokens.TRANSLATION);
		settingsMenuBar.addMenuItem(messages.chequePrintSetting(),
				HistoryTokens.CHECK_PRINT_SETTING);

		return settingsMenuBar;
	}

	private Menu getFixedAssetsMenu(String string) {
		Menu fixedAssetMenu = new Menu(string);

		fixedAssetMenu.addMenuItem(messages.newFixedAsset(),
				HistoryTokens.NEWFIXEDASSETS);
		fixedAssetMenu.addSeparatorItem();
		fixedAssetMenu.addMenuItem(messages.depreciation(),
				HistoryTokens.DEPRICATION);

		fixedAssetMenu.addSeparatorItem();

		fixedAssetMenu.addMenuItem(messages.pendingItemsList(),
				HistoryTokens.PENDINGITEMS);
		fixedAssetMenu.addMenuItem(messages.registeredItemsList(),
				HistoryTokens.REGISTEREDITEMS);

		fixedAssetMenu.addMenuItem(messages.soldDisposedItems(),
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);

		return fixedAssetMenu;
	}

	private Menu getVATMenu(String string) {
		Menu vatmenu = new Menu(string);

		Menu vatNews = new Menu(string);

		if (canDoInvoiceTransactions) {
			vatNews.addMenuItem(messages.newTaxItem(), HistoryTokens.NEWTAXITEM);
			vatNews.addMenuItem(messages.newTaxCode(), HistoryTokens.NEWVATCODE);
			vatNews.addMenuItem(messages.newTAXAgency(),
					HistoryTokens.NEWTAXAGENCY);

			vatmenu.addMenuItem(vatNews);

			vatmenu.addSeparatorItem();
		}

		if (canDoInvoiceTransactions) {
			vatmenu.addMenuItem(messages.taxAdjustment(),
					HistoryTokens.TAXADJUSTMENT);
			vatmenu.addMenuItem(messages.fileTAX(), HistoryTokens.FILETAX);
		}

		if (canDoBanking) {
			vatmenu.addMenuItem(messages.payTax(), HistoryTokens.PAYTAX);
			vatmenu.addMenuItem(messages.tAXRefund(), HistoryTokens.TAXREFUND);
			vatmenu.addMenuItem(messages.taxHistory(), HistoryTokens.TAXHISTORY);
		}
		vatmenu.addSeparatorItem();
		vatmenu.addMenuItem(getVATsListMenu(messages.taxList()));

		return vatmenu;
	}

	private Menu getVATsListMenu(String string) {
		Menu vatmenus = new Menu(string);

		vatmenus.addMenuItem(messages.taxItemsList(), HistoryTokens.VATITEMS);
		vatmenus.addMenuItem(messages.taxCodesList(), HistoryTokens.VATCODES);
		vatmenus.addMenuItem(messages.payeesList(messages.taxAgencies()),
				HistoryTokens.TAXAGENCYLIST);

		return vatmenus;
	}

	private Menu getFixedAssetsListMenu(String string) {
		Menu fixedAssetListMenu = new Menu(string);

		fixedAssetListMenu.addMenuItem(messages.pendingItemsList(),
				HistoryTokens.PENDINGITEMS);
		fixedAssetListMenu.addMenuItem(messages.registeredItemsList(),
				HistoryTokens.REGISTEREDITEMS);

		fixedAssetListMenu.addMenuItem(messages.soldDisposedItems(),
				HistoryTokens.SOLIDDISPOSEDFIXEDASSETS);
		return fixedAssetListMenu;
	}

	private Menu getReportMenu(String string) {
		Menu reportMenuBar = new Menu(string);

		reportMenuBar.addMenuItem(messages.reportsHome(),
				HistoryTokens.REPORTHOME);

		reportMenuBar.addSeparatorItem();

		reportMenuBar.addMenuItem(getCompanyAndFinancialMenu(messages
				.companyAndFinance()));

		reportMenuBar.addMenuItem(getCustomersAndReceivableMenu(messages
				.customersAndReceivable(Global.get().Customers())));

		reportMenuBar.addMenuItem(getSalesMenu(messages.sales()));

		reportMenuBar.addMenuItem(getVendorAndPayablesMenu(messages
				.vendorsAndPayables(Global.get().Vendors())));

		reportMenuBar.addMenuItem(getPurchaseMenu(messages.purchase()));

		reportMenuBar.addMenuItem(getBudgetSubMenus(messages.budget()));

		if (isTrackTax) {
			reportMenuBar.addMenuItem(getVATReportMenu(messages.tax()));
		}
		reportMenuBar.addMenuItem(getFixedAssetReportSubMenu(messages
				.fixedAssest()));

		return reportMenuBar;
	}

	private MenuItem getFixedAssetReportSubMenu(String fixedAssest) {
		Menu fixedAssetsReportMenu = new Menu(fixedAssest);
		fixedAssetsReportMenu.addMenuItem(messages.depreciation(),
				HistoryTokens.DEPRECIATIONSHEDULE);
		return fixedAssetsReportMenu;
	}

	private Menu getSalesSubMenu(String string) {
		Menu salesMenu = new Menu(string);

		if (canDoInvoiceTransactions) {
			salesMenu.addMenuItem(messages.salesOrder(),
					HistoryTokens.SALESORDER);
		}
		if (canSeeInvoiceTransactions) {
			salesMenu.addMenuItem(messages.salesOrderList(),
					HistoryTokens.SALESORDERLIST);
		}
		if (canViewReports) {
			salesMenu.addMenuItem(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}

		return salesMenu;
	}

	private Menu getPurchaseSubMenu(String string) {
		Menu purchaseMenu = new Menu(string);

		if (canDoInvoiceTransactions) {
			purchaseMenu.addMenuItem(messages.purchaseOrder(),
					HistoryTokens.PURCHASEORDER);
		}
		if (canSeeInvoiceTransactions) {
			purchaseMenu.addMenuItem(messages.purchaseOrderList(),
					HistoryTokens.PURCHASEORDERLIST);
		}
		if (canViewReports) {
			purchaseMenu.addMenuItem(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}

		return purchaseMenu;
	}

	private Menu getVendorAndPayablesMenu(String string) {

		Menu vendorAndPayableMenuBar = new Menu(string);

		vendorAndPayableMenuBar.addMenuItem(messages.apAgeingSummary(),
				HistoryTokens.APAGINGSUMMARY);
		vendorAndPayableMenuBar.addMenuItem(messages.apAgeingDetail(),
				HistoryTokens.APAGINGDETAIL);
		vendorAndPayableMenuBar.addMenuItem(
				messages.payeeStatement(Global.get().Vendors()),
				HistoryTokens.VENDORSTATEMENT);
		vendorAndPayableMenuBar.addMenuItem(
				messages.payeeTransactionHistory(Global.get().Vendor()),
				HistoryTokens.VENDORTRANSACTIONHISTORY);

		return vendorAndPayableMenuBar;
	}

	private Menu getBudgetSubMenus(String string) {
		Menu budgetMenu = new Menu(string);

		budgetMenu.addMenuItem(messages.budgetOverview(),
				HistoryTokens.BUDGETREPORTOVERVIEW);

		return budgetMenu;
	}

	private Menu getPurchaseMenu(String string) {
		Menu purchaseMenuBar = new Menu(string);

		purchaseMenuBar.addMenuItem(
				messages.purchaseByVendorSummary(Global.get().Vendor()),
				HistoryTokens.PURCHASEBYVENDORSUMMARY);
		purchaseMenuBar.addMenuItem(
				messages.purchaseByVendorDetail(Global.get().Vendor()),
				HistoryTokens.PURCHASEBYVENDORDETAIL);
		purchaseMenuBar.addMenuItem(messages.purchaseByItemSummary(),
				HistoryTokens.PURCHASEBYITEMSUMMARY);
		purchaseMenuBar.addMenuItem(messages.purchaseByItemDetail(),
				HistoryTokens.PURCHASEBYITEMDETAIL);
		if (isPurchaseOrderEnabled) {
			purchaseMenuBar.addMenuItem(messages.purchaseOrderReport(),
					HistoryTokens.PURCHASEORDERREPORT);
		}

		return purchaseMenuBar;
	}

	private Menu getVATReportMenu(String string) {

		Menu vatReportMenuBar = new Menu(string);

		if (company instanceof UnitedKingdom) {

			vatReportMenuBar.addMenuItem(messages.priorVATReturns(),
					HistoryTokens.PRIORVATRETURN);
			vatReportMenuBar.addMenuItem(messages.vatDetail(),
					HistoryTokens.VATDETAIL);
			vatReportMenuBar.addMenuItem(messages.vat100(),
					HistoryTokens.VAT100);
			vatReportMenuBar.addMenuItem(messages.uncategorisedVATAmounts(),
					HistoryTokens.UNCATEGORISEDVATAMOUNT);
			vatReportMenuBar.addMenuItem(messages.ecSalesList(),
					HistoryTokens.ECSALESLIST);

		} else {
			vatReportMenuBar.addMenuItem(messages.taxItemDetailReport(),
					HistoryTokens.TAXITEMDETAIL);
			vatReportMenuBar.addMenuItem(
					messages.taxItemExceptionDetailReport(),
					HistoryTokens.TAXITEMEXCEPTIONDETAILS);
		}
		vatReportMenuBar.addMenuItem(messages.vatItemSummary(),
				HistoryTokens.VATITEMSUMMARY);

		return vatReportMenuBar;
	}

	private Menu getSalesMenu(String string) {
		Menu salesMenuBar = new Menu(string);

		salesMenuBar.addMenuItem(
				messages.salesByCustomerSummary(Global.get().Customer()),
				HistoryTokens.SALESBYCUSTOMERSUMMARY);
		salesMenuBar.addMenuItem(
				messages.salesByCustomerDetail(Global.get().Customer()),
				HistoryTokens.SALESBYCUSTOMERDETAIL);
		salesMenuBar.addMenuItem(messages.salesByItemSummary(),
				HistoryTokens.SALESBYITEMSUMMARY);
		salesMenuBar.addMenuItem(messages.salesByItemDetail(),
				HistoryTokens.SALESBYITEMDETAIL);

		if (isSalesOrderEnabled) {
			salesMenuBar.addMenuItem(messages.salesOrderReport(),
					HistoryTokens.SALESORDERREPORT);
		}
		if (isLocationTrackingEnabled) {
			salesMenuBar
					.addMenuItem(messages.getSalesByLocationDetails(Global
							.get().Location()),
							HistoryTokens.SALESBYCLASSDETAILS);
			salesMenuBar.addMenuItem(
					messages.salesByLocationSummary(Global.get().Location()),
					HistoryTokens.SALESBYCLASSSUMMARY);
		}

		if (isClassTrackingEnabled) {
			salesMenuBar.addMenuItem(messages.salesByClassDetails(),
					HistoryTokens.SALESBYLOCATIONDETAILS);
			salesMenuBar.addMenuItem(messages.salesByClassSummary(),
					HistoryTokens.SALESBYLOCATIONSUMMARY);
		}

		return salesMenuBar;
	}

	private Menu getCustomersAndReceivableMenu(String string) {
		Menu customersAndReceivableMenuBar = new Menu(string);

		customersAndReceivableMenuBar.addMenuItem(messages.arAgeingSummary(),
				HistoryTokens.ARAGINGSUMMARY);
		customersAndReceivableMenuBar.addMenuItem(messages.arAgeingDetail(),
				HistoryTokens.ARAGINGDETAIL);
		customersAndReceivableMenuBar.addMenuItem(
				messages.payeeStatement(Global.get().Customers()),
				HistoryTokens.CUSTOMERSTATEMENT);
		customersAndReceivableMenuBar.addMenuItem(
				messages.payeeTransactionHistory(messages.Customer()),
				HistoryTokens.CUSTOMERTRANSACTIONHISTORY);

		return customersAndReceivableMenuBar;
	}

	private Menu getCompanyAndFinancialMenu(String string) {

		Menu companyAndFinancialMenuBar = new Menu(string);

		companyAndFinancialMenuBar.addMenuItem(messages.profitAndLoss(),
				HistoryTokens.PROFITANDLOSS);
		companyAndFinancialMenuBar.addMenuItem(messages.balanceSheet(),
				HistoryTokens.BALANCESHEET);
		companyAndFinancialMenuBar.addMenuItem(messages.cashFlowReport(),
				HistoryTokens.CASHFLOWREPORT);
		companyAndFinancialMenuBar.addMenuItem(messages.trialBalance(),
				HistoryTokens.TRIALBALANCE);
		companyAndFinancialMenuBar.addMenuItem(
				messages.transactionDetailByAccount(),
				HistoryTokens.TRANSACTIONDETAILBYACCOUNT);

		companyAndFinancialMenuBar.addMenuItem(messages.generalLedgerReport(),
				HistoryTokens.GENERALLEDGER);

		companyAndFinancialMenuBar.addMenuItem(messages.expenseReport(),
				HistoryTokens.EXPENSEREPORT);
		if (isTaxTracking) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.salesTaxLiability(),
					HistoryTokens.SALESTAXLIABILITY);
			companyAndFinancialMenuBar.addMenuItem(
					messages.transactionDetailByTaxItem(),
					HistoryTokens.TRANSACTIONDETAILBYTAXITEM);
		}
		if (isLocationTrackingEnabled) {
			companyAndFinancialMenuBar.addMenuItem(messages.profitAndLoss()
					+ "By" + Global.get().Location(),
					HistoryTokens.PROFITANDLOSSBYLOCATION);
		}
		if (isClassTrackingEnabled) {
			companyAndFinancialMenuBar.addMenuItem(
					messages.profitAndLossbyClass(),
					HistoryTokens.PROFITANDLOSSBYCLASS);
		}

		return companyAndFinancialMenuBar;
	}

	private Menu getBankingMenu(String string) {
		Menu bankingMenuBar = new Menu(string);

		bankingMenuBar.addMenuItem(messages.newBankAccount(),
				HistoryTokens.NEWBANKACCOUNT);
		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(messages.writeCheck(),
				HistoryTokens.WRITECHECK);
		bankingMenuBar.addMenuItem(messages.makeDeposit(),
				HistoryTokens.DEPOSITETRANSFERFUNDS);

		if (isKeepTrackofBills) {
			bankingMenuBar.addMenuItem(messages.payBills(),
					HistoryTokens.PAYBILL);
		}

		bankingMenuBar.addSeparatorItem();
		bankingMenuBar.addMenuItem(messages.ReconciliationsList(),
				HistoryTokens.RECOUNCILATIONSLIST);

		bankingMenuBar.addSeparatorItem();

		bankingMenuBar.addMenuItem(getBankingListMenu(messages.bankingList()));

		return bankingMenuBar;
	}

	private Menu getBankingListMenu(String string) {

		Menu bankingListMenuBar = new Menu(string);

		bankingListMenuBar.addMenuItem(messages.payments(),
				HistoryTokens.PAYMENTS);
		bankingListMenuBar.addMenuItem(messages.bankAccount(),
				HistoryTokens.BANKACCOUNTS);

		return bankingListMenuBar;
	}

	private Menu getVendorMenu(String string) {

		Menu vendorMenuBar = new Menu(string);

		vendorMenuBar.addMenuItem(messages.payeesHome(Global.get().Vendors()),
				HistoryTokens.VENDOREHOME);

		vendorMenuBar.addSeparatorItem();

		vendorMenuBar.addMenuItem(getNewVendorMenu(messages.new1()));
		vendorMenuBar.addSeparatorItem();

		if (canDoInvoiceTransactions) {
			if (isKeepTrackofBills) {
				vendorMenuBar.addMenuItem(messages.enterBill(),
						HistoryTokens.ENTERBILL);
			}
		}
		if (canDoBanking) {
			if (isKeepTrackofBills) {

				vendorMenuBar.addMenuItem(messages.payBills(),
						HistoryTokens.PAYBILL);
				vendorMenuBar.addMenuItem(messages.issuePayments(),
						HistoryTokens.ISSUEPAYMENTS);
				// vendorMenuBar.addMenuItem(messages.printCheque(),
				// HistoryTokens.PRINTCHEQUE);
			}
			vendorMenuBar.addMenuItem(
					messages.payeePrePayment(Global.get().Vendor()),
					HistoryTokens.VENDORPREPAYMENT);
		}
		if (canDoInvoiceTransactions) {
			vendorMenuBar.addMenuItem(messages.recordExpenses(),
					HistoryTokens.RECORDEXPENSES);

			if (isHaveEpmloyees && isTrackEmployeeExpenses) {
				vendorMenuBar.addMenuItem(messages.expenseClaims(),
						HistoryTokens.EXPENSECLAIMS);
			}
			vendorMenuBar.addSeparatorItem();
		}
		vendorMenuBar.addMenuItem(getVendorListMenu(messages.payeeLists(Global
				.get().Vendor())));

		return vendorMenuBar;
	}

	private Menu getVendorListMenu(String string) {
		Menu vendorListMenuBar = new Menu(string);

		vendorListMenuBar.addMenuItem(messages.payees(Global.get().Vendors()),
				HistoryTokens.VENDORLIST);

		if (canSeeInvoiceTransactions) {
			vendorListMenuBar.addMenuItem(
					messages.payees(Global.get().Vendors()) + " "
							+ messages.items(), HistoryTokens.VENDORITEMS);

			if (isKeepTrackofBills) {
				vendorListMenuBar.addMenuItem("Bills And Expenses",
						HistoryTokens.BILLSANDEXPENSES);
			}
		}
		if (canSeeBanking) {
			vendorListMenuBar.addMenuItem(
					messages.payeePayment(Global.get().Vendor()),
					HistoryTokens.VENDORPAYMENTS);
		}

		return vendorListMenuBar;
	}

	private Menu getNewVendorMenu(String string) {

		Menu newVendorMenuBar = new Menu(string);

		if (canDoInvoiceTransactions) {
			newVendorMenuBar.addMenuItem(
					messages.newPayee(Global.get().Vendor()),
					HistoryTokens.NEWVENDOR);
			newVendorMenuBar.addMenuItem(messages.newItem() + "s",
					HistoryTokens.NEWITEMSUPPLIERS);
		}
		if (canDoBanking) {
			newVendorMenuBar.addMenuItem(messages.cashPurchase(),
					HistoryTokens.NEWCASHPURCHASE);
		}
		if (canDoInvoiceTransactions) {

			newVendorMenuBar.addMenuItem(
					messages.payeeCredit(Global.get().Vendor()),
					HistoryTokens.VENDORCREDIT);

			newVendorMenuBar.addMenuItem(messages.newCheck(),
					HistoryTokens.CHECK);
		}

		return newVendorMenuBar;
	}

	private Menu getCustomerMenu(String string) {
		Menu customerMenuBar = new Menu(string);

		customerMenuBar.addMenuItem(
				messages.payeesHome(Global.get().Customers()),
				HistoryTokens.CUSTOMERHOME);

		customerMenuBar.addSeparatorItem();

		customerMenuBar.addMenuItem(getNewCustomerMenu(messages.new1()));

		customerMenuBar.addSeparatorItem();

		if (canDoBanking) {

			customerMenuBar.addMenuItem(
					messages.payeePrePayment(Global.get().Customer()),
					HistoryTokens.CUSTOMERPREPAYMENT);
			customerMenuBar.addMenuItem(messages.receivePayment(),
					HistoryTokens.RECEIVEPAYMENT);
			customerMenuBar.addMenuItem(
					messages.customerRefund(Global.get().Customer()),
					HistoryTokens.CUSTOMERREFUND);

			customerMenuBar.addSeparatorItem();
		}
		customerMenuBar.addMenuItem(getCustomerListMenu(messages
				.payeeLists(Global.get().Customer())));

		return customerMenuBar;
	}

	private Menu getCustomerListMenu(String string) {
		Menu customerListMenuBar = new Menu(string);

		customerListMenuBar.addMenuItem(
				messages.payees(Global.get().Customers()),
				HistoryTokens.CUSTOMERS);
		if (canSeeInvoiceTransactions) {
			customerListMenuBar.addMenuItem(
					messages.payees(Global.get().Customers()) + " "
							+ messages.items(), HistoryTokens.CUSTOMERITEMS);
			if (isDoyouwantEstimates) {
				customerListMenuBar.addMenuItem(messages.quotes(),
						HistoryTokens.QUOTES);
			}

			if (isDelayedchargesEnabled) {
				customerListMenuBar.addMenuItem(messages.Charges(),
						HistoryTokens.CHARGES);
				customerListMenuBar.addMenuItem(messages.credits(),
						HistoryTokens.CREDITS);
			}

			customerListMenuBar.addMenuItem(messages.invoices(),
					HistoryTokens.INVOICES);
		}
		if (canSeeBanking) {
			customerListMenuBar.addMenuItem(messages.receivedPayments(),
					HistoryTokens.RECEIVEPAYMENTS);
			customerListMenuBar.addMenuItem(
					messages.customerRefunds(Global.get().Customer()),
					HistoryTokens.CUSTOMERREFUNDS);
			customerListMenuBar.addMenuItem(messages.transactionscenter(),
					HistoryTokens.TRANSACTIONS_CENTER);
		}

		return customerListMenuBar;
	}

	private Menu getNewCustomerMenu(String string) {
		Menu newCustomerMenuBar = new Menu(string);

		if (canDoInvoiceTransactions) {
			newCustomerMenuBar.addMenuItem(
					messages.newPayee(Global.get().Customer()),
					HistoryTokens.NEWCUSTOMER);
			newCustomerMenuBar.addMenuItem(messages.newItem(),
					HistoryTokens.NEWITEMCUSTOMER);
			if (isDoyouwantEstimates) {
				newCustomerMenuBar.addMenuItem(messages.newQuote(),
						HistoryTokens.NEWQUOTE);
			}

			if (isDelayedchargesEnabled) {
				newCustomerMenuBar.addMenuItem(messages.newCharge(),
						HistoryTokens.NEWCHARGE);
				newCustomerMenuBar.addMenuItem(messages.newCredit(),
						HistoryTokens.NEWCREDIT);
			}

			newCustomerMenuBar.addMenuItem(messages.newInvoice(),
					HistoryTokens.NEWINVOICE);
		}

		if (canDoBanking) {
			newCustomerMenuBar.addMenuItem(messages.newCashSale(),
					HistoryTokens.NEWCASHSALE);
		}
		if (canDoInvoiceTransactions) {
			newCustomerMenuBar.addMenuItem(messages.newCreditMemo(),
					HistoryTokens.NRECREDITNOTE);
		}

		return newCustomerMenuBar;
	}

	private Menu getCompanyMenu(String string) {

		Menu companyMenuBar = new Menu(string);

		companyMenuBar.addMenuItem(messages.dashBoard(),
				HistoryTokens.DASHBOARD);

		companyMenuBar.addSeparatorItem();

		companyMenuBar.addMenuItem(messages.search(), HistoryTokens.SEARCH);

		companyMenuBar.addSeparatorItem();

		if (canDoBanking)
			companyMenuBar.addMenuItem(messages.journalEntry(),
					HistoryTokens.NEWJOURNALENTRY);

		if (canDoInvoiceTransactions) {
			companyMenuBar.addMenuItem(messages.newPayee(messages.Account()),
					HistoryTokens.NEWACCOUNT);
			companyMenuBar.addSeparatorItem();
		}

		if (canChangeSettings) {
			companyMenuBar.addMenuItem(messages.companyPreferences(),
					HistoryTokens.COMPANYPREFERENCES);
			companyMenuBar.addSeparatorItem();
		}
		companyMenuBar.addMenuItem(messages.budget(), HistoryTokens.BUDGET);

		companyMenuBar.addSeparatorItem();

		if (isTrackTax) {
			companyMenuBar.addMenuItem(getSalesTaxSubmenu(messages.itemTax()));
		}
		if (canChangeSettings) {
			companyMenuBar.addMenuItem(getManageSupportListSubmenu(messages
					.manageSupportLists()));
			companyMenuBar.addSeparatorItem();
		}
		companyMenuBar.addMenuItem(getFixedAssetsMenu(messages.fixedAssets()));
		companyMenuBar.addSeparatorItem();
		companyMenuBar.addMenuItem(getMergeSubMenu(messages.mergeAccounts()));
		companyMenuBar.addSeparatorItem();
		companyMenuBar.addMenuItem(getCompanyListMenu(messages.companyLists()));

		return companyMenuBar;
	}

	private Menu getMergeSubMenu(String string) {
		Menu mergeAccountsMenuBar = new Menu(string);

		mergeAccountsMenuBar.addMenuItem(
				messages.mergeCustomers(Global.get().Customers()),
				HistoryTokens.MERGECUSTOMERS);
		mergeAccountsMenuBar.addMenuItem(
				messages.mergeVendors(Global.get().Vendors()),
				HistoryTokens.MERGEVENDOR);
		mergeAccountsMenuBar.addMenuItem(messages.mergeAccounts(),
				HistoryTokens.MERGEACCOUNT);
		mergeAccountsMenuBar.addMenuItem(messages.mergeItems(),
				HistoryTokens.MERGEITEM);

		return mergeAccountsMenuBar;
	}

	private Menu getCompanyListMenu(String string) {
		Menu companyListMenuBar = new Menu(string);

		if (canSeeInvoiceTransactions) {
			companyListMenuBar.addMenuItem(
					messages.payeeList(messages.Accounts()),
					HistoryTokens.ACCOUNTSLIST);
		}
		if (canSeeBanking) {
			companyListMenuBar.addMenuItem(messages.journalEntries(),
					HistoryTokens.JOURNALENTRIES);
		}

		if (canSeeInvoiceTransactions) {
			companyListMenuBar.addMenuItem(messages.items(),
					HistoryTokens.ALLITEMS);
		}
		companyListMenuBar.addMenuItem(Global.get().Customer(),
				HistoryTokens.CUSTOMERS);
		companyListMenuBar.addMenuItem(Global.get().Vendor(),
				HistoryTokens.VENDORLIST);
		if (canSeeBanking) {
			companyListMenuBar.addMenuItem(messages.payments(),
					HistoryTokens.PAYMENTS);
		}
		companyListMenuBar.addMenuItem(messages.salesPersons(),
				HistoryTokens.SALESPRESONS);
		companyListMenuBar.addMenuItem(messages.usersActivityLogTitle(),
				HistoryTokens.USERACTIVITY);

		return companyListMenuBar;
	}

	private Menu getManageSupportListSubmenu(String string) {
		Menu manageSupportListMenuBar = new Menu(string);

		manageSupportListMenuBar.addMenuItem(
				messages.payeeGroupList(Global.get().Customer()),
				HistoryTokens.CUSTOMERGROUPLIST);
		manageSupportListMenuBar.addMenuItem(
				messages.payeeGroupList(Global.get().vendor()),
				HistoryTokens.VENDORGROUPLIST);
		manageSupportListMenuBar.addMenuItem(messages.paymentTermList(),
				HistoryTokens.PAYMENTTERMS);
		manageSupportListMenuBar.addMenuItem(messages.shippingMethodList(),
				HistoryTokens.SHIPPINGMETHODSLIST);
		manageSupportListMenuBar.addMenuItem(messages.shippingTermList(),
				HistoryTokens.SHIPPINGTERMSLIST);

		manageSupportListMenuBar.addMenuItem(messages.itemGroupList(),
				HistoryTokens.ITEMGROUPLIST);
		manageSupportListMenuBar.addMenuItem(messages.currencyList(),
				HistoryTokens.CURRENCYGROUPLIST);
		if (isClassTracking) {
			manageSupportListMenuBar.addMenuItem(messages.accounterClassList(),
					HistoryTokens.ACCOUNTERCLASSLIST);
		}
		if (isLocationTracking) {
			manageSupportListMenuBar.addMenuItem(messages.locationsList(),
					HistoryTokens.LOCATIONGROUPLIST);
		}
		return manageSupportListMenuBar;

	}

	private Menu getSalesTaxSubmenu(String string) {

		Menu salesTaxMenuBar = new Menu(string);

		if (canDoInvoiceTransactions) {
			salesTaxMenuBar.addMenuItem(messages.manageSalesTaxGroups(),
					HistoryTokens.MANAGESALESTAXGROUP);
		} else {
			salesTaxMenuBar.addMenuItem(messages.salesTaxGroups(),
					HistoryTokens.SALESTAXGROUPsalesTaxGroup);
		}
		if (canDoInvoiceTransactions) {
			salesTaxMenuBar.addMenuItem(messages.manageSalesItems(),
					HistoryTokens.MANAGESALESTAXITEMS);
		} else {
			salesTaxMenuBar.addMenuItem(messages.salesTaxItems(),
					HistoryTokens.SALESTAXITEMS);
		}
		if (canDoInvoiceTransactions) {
			salesTaxMenuBar.addMenuItem(messages.taxAdjustment(),
					HistoryTokens.TAXADJUSTMENT);
		}
		if (canDoBanking) {
			salesTaxMenuBar.addMenuItem(messages.payTax(),
					HistoryTokens.PAYSALESTAX);
		}
		if (canDoInvoiceTransactions) {
			salesTaxMenuBar.addMenuItem(messages.newTAXAgency(),
					HistoryTokens.NEWTAXAGENCY);
		}
		return salesTaxMenuBar;
	}

	public void setPreferencesandPermissions(
			ClientCompanyPreferences preferences, ClientUser clientUser,
			ICountryPreferences countryPreferences) {

		this.canDoInvoiceTransactions = canDoInvoiceTransactions(clientUser);

		this.canChangeSettings = canChangeSettings(clientUser);

		this.isTrackTax = preferences.isTrackTax();

		this.isLocationTracking = preferences.isLocationTrackingEnabled();

		this.canDoBanking = canDoBanking(clientUser);

		this.canManageFiscalYears = canManageFiscalYears;

		this.isClassTracking = preferences.isClassTrackingEnabled();

		this.canSeeBanking = canSeeBanking(clientUser);

		this.canSeeInvoiceTransactions = canSeeInvoiceTransactions(clientUser);

		this.isDoyouwantEstimates = preferences.isDoyouwantEstimates();

		this.isDelayedchargesEnabled = preferences.isDelayedchargesEnabled();

		this.isHaveEpmloyees = preferences.isHaveEpmloyees();

		this.isTrackEmployeeExpenses = preferences.isTrackEmployeeExpenses();

		this.isKeepTrackofBills = preferences.isKeepTrackofBills();

		this.canViewReports = canViewReports(clientUser);

		this.iswareHouseEnabled = preferences.iswareHouseEnabled();

		this.isPurchaseOrderEnabled = preferences.isPurchaseOrderEnabled();

		this.isSalesOrderEnabled = preferences.isSalesOrderEnabled();

		this.isClassTrackingEnabled = preferences.isClassTrackingEnabled();

		this.isLocationTrackingEnabled = preferences
				.isLocationTrackingEnabled();

		this.isTaxTracking = preferences.isTrackTax();

		this.isInventoryEnabled = preferences.isInventoryEnabled();

		this.company = countryPreferences;

		getMenuBar();
	}

	private boolean canManageFiscalYears(ClientUser user) {
		if (user.getPermissions().getTypeOfLockDates() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canDoInvoiceTransactions(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfInvoices() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canChangeSettings(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfSystemSettings() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canViewReports(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_YES
				|| clientUser.getPermissions().getTypeOfViewReports() == RolePermissions.TYPE_READ_ONLY)
			return true;
		else
			return false;
	}

	private boolean canDoBanking(ClientUser clientUser) {
		if (clientUser.getPermissions().getTypeOfBankReconcilation() == RolePermissions.TYPE_YES)
			return true;
		else
			return false;
	}

	private boolean canSeeBanking(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfBankReconcilation() != RolePermissions.TYPE_NO;
	}

	private boolean canSeeInvoiceTransactions(ClientUser clientUser) {
		return clientUser.getPermissions().getTypeOfInvoices() != RolePermissions.TYPE_NO;
	}

}
