package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientReconciliationItem;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.TransactionsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.BankStatementGrid;
import com.vimukti.accounter.web.client.ui.grids.ReconcilListGrid;

public class StatementTransactionsReconcileView extends
		BaseView<ClientReconciliation> {

	private BankStatementGrid bankStatementGrid;
	private DecoratedTabPanel tabSet;
	private AmountLabel closebalanceLable, clearedbalance, difference;
	private ClientAccount account;
	private AccounterMessages messages;
	private VerticalPanel Vpanel, main_Vpanel;
	private double totalAmount = 0.0;
	private ReconcilListGrid reconcilListGrid;
	protected ClientStatement statement;
	private PaginationList<TransactionsList> allRecords;
	double spentTotal = 0.0;
	double receivedTotal = 0.0;

	public StatementTransactionsReconcileView(long accountid,
			ClientStatement statement) {
		super();
		// getAllTransactions();
		this.account = getCompany().getAccount(accountid);
		this.statement = statement;

	}

	/**
	 * gets all the received and spent transactions
	 */
	private void getAllTransactions() {
		rpcUtilService.getSpentAndReceivedTransactionsList(
				new ClientFinanceDate().getDate(),
				new ClientFinanceDate().getDate(), 0, 10,
				new AsyncCallback<PaginationList<TransactionsList>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					public void onSuccess(
							PaginationList<TransactionsList> result) {
						if (result != null) {
							addRecordsToGrid(result);
						}
					}

				});

	}

	/**
	 * adds all statementRecords to grid, by comparing the amount with all the
	 * spent and received amounts
	 * 
	 * @param matchedRecords
	 */
	private void addRecordsToGrid(
			PaginationList<TransactionsList> matchedRecords) {
		int count = 0;
		PaginationList<ClientStatementRecord> list = new PaginationList<ClientStatementRecord>();
		List<ClientStatementRecord> stList = statement.getStatementList();
		Iterator iterator = stList.iterator();
		while ((iterator).hasNext()) {
			ClientStatementRecord obj = (ClientStatementRecord) iterator.next();
			list.add(obj);
			if (obj.isMatched()) {
				count++;
			}
		}
		if (stList.size() == count) {
			saveAndCloseButton.setVisible(true);
		}
		reconcilListGrid.setRecords(list);
	}

	@Override
	public void init() {
		super.init();
		this.messages = Global.get().messages();
		setMode(EditMode.CREATE);
		saveAndCloseButton.setText("Publish");
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		createControls();
		setSize("100%", "100%");
	}

	/**
 * 
 */
	private void createControls() {
		Vpanel = new VerticalPanel();
		showBankDetails();
		tabSet = new DecoratedTabPanel();
		new ArrayList<DynamicForm>();
		tabSet.add(getReconcileTab(), "Reconcile");
		tabSet.add(getBankStatementsTab(), "Bank transactions");
		tabSet.selectTab(0);
		tabSet.setSize("100%", "100%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(tabSet);
		Vpanel.add(mainVLay);
		this.add(Vpanel);
		// setSize("100%", "100%");
	}

	/**
	 * this method is used to show the bank details and statementDetails
	 */
	private void showBankDetails() {
		HorizontalPanel hPanel = new HorizontalPanel();

		ClientAccount clientAccount = Accounter.getCompany().getAccount(
				statement.getAccount());
		// for bank name
		VerticalPanel invoicePanel = new VerticalPanel();
		HTML bankAccountlabel = new HTML("<p>" + messages.bankAccount()
				+ "<br/><br/></p>" + clientAccount.getDisplayName() + "</p>");
		invoicePanel.add(bankAccountlabel);
		invoicePanel.setStyleName("rightBorder");
		invoicePanel.setSpacing(10);

		// for matched records total balance
		VerticalPanel accBalancePanel = new VerticalPanel();
		HTML accBalancelabel = new HTML("<p>" + "Balance " + "<br/><br/></p>"
				+ getBalanceAmount(true) + "</p>");
		accBalancePanel.add(accBalancelabel);
		accBalancePanel.setStyleName("rightBorder");
		accBalancePanel.setSpacing(10);

		// for no of reconcile records
		VerticalPanel reconcilePanel = new VerticalPanel();
		HTML reconcile = new HTML("<p>" + messages.Reconcile()
				+ "<br/><br/></p>" + statement.getStatementList().size()
				+ messages.itemsToBeReconciled() + "</p>");
		reconcilePanel.add(reconcile);
		reconcilePanel.setStyleName("rightBorder");
		reconcilePanel.setSpacing(10);

		// for statement balance
		VerticalPanel balancePanel = new VerticalPanel();
		HTML balance = new HTML("<p>" + messages.statementBalance()
				+ "<br/><br/></p>" + getBalanceAmount(false) + "</p>");
		balancePanel.add(balance);
		balancePanel.setStyleName("rightBorder");
		balancePanel.setSpacing(10);

		// for statement imported date
		VerticalPanel importedDatePanel = new VerticalPanel();
		HTML importedDate = new HTML("<p>" + messages.statementsImportedDate()
				+ "<br/><br/></p>" + statement.getImporttedDate() + "</p>");
		importedDatePanel.add(importedDate);
		importedDatePanel.setStyleName("rightBorder");
		importedDatePanel.setSpacing(10);

		// for starting balance
		VerticalPanel startingBalancePanel = new VerticalPanel();
		HTML startBalance = new HTML("<p>" + messages.startingBalance()
				+ "<br/><br/></p>" + getStartingBalance() + "</p>");
		startingBalancePanel.add(startBalance);
		startingBalancePanel.setStyleName("rightBorder");
		startingBalancePanel.setSpacing(10);

		// for closing balance
		VerticalPanel closingBalancePanel = new VerticalPanel();
		HTML closeBalance = new HTML("<p>" + messages.ClosingBalance()
				+ "<br/><br/></p>" + statement.getClosingBalance() + "</p>");
		closingBalancePanel.add(closeBalance);
		closingBalancePanel.setSpacing(10);

		hPanel.add(invoicePanel);
		hPanel.add(accBalancePanel);
		hPanel.add(reconcilePanel);
		hPanel.add(balancePanel);
		hPanel.add(importedDatePanel);
		hPanel.add(startingBalancePanel);
		hPanel.add(closingBalancePanel);

		Vpanel.add(hPanel);

	}

	private double getBalanceAmount(boolean forMatched) {
		// for displaying balance
		double balanceAmt = 0;
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		for (ClientStatementRecord stRecord : statementList) {
			if (forMatched) {
				if (stRecord.isMatched()) {
					if (stRecord.getSpentAmount() > 0) {
						balanceAmt += stRecord.getSpentAmount();
					} else {
						balanceAmt += stRecord.getReceivedAmount();
					}
				}
			} else {
				if (stRecord.getSpentAmount() > 0) {
					balanceAmt += stRecord.getSpentAmount();
				} else {
					balanceAmt += stRecord.getReceivedAmount();
				}
			}

		}

		return balanceAmt;
	}

	/**
	 * used to get the starting balance
	 * 
	 * @return
	 */
	private double getStartingBalance() {
		ClientStatementRecord stRec = statement.getStatementList().get(0);
		double startBalance = 0;
		if (stRec.getSpentAmount() > 0) {
			startBalance = stRec.getClosingBalance() + stRec.getSpentAmount();
		} else {
			startBalance = stRec.getClosingBalance()
					- stRec.getReceivedAmount();
		}
		startBalance = Utility.roundTo2Digits(startBalance);
		return startBalance;
	}

	/**
	 * 
	 * @return
	 */
	private VerticalPanel getBankStatementsTab() {

		VerticalPanel mainPanel = new VerticalPanel();

		DynamicForm amountsForm = new DynamicForm();

		// transactions grid
		this.bankStatementGrid = new BankStatementGrid();
		bankStatementGrid.init();
		bankStatementGrid.setWidth("100%");
		bankStatementGrid.addLoadingImagePanel();
		bankStatementGrid.setRecords(statement.getStatementList());

		// AmountLables
		closebalanceLable = new AmountLabel(messages.ClosingBalance());
		closebalanceLable.setHelpInformation(true);
		closebalanceLable.setDisabled(true);

		clearedbalance = new AmountLabel(messages.ClearedAmount());
		clearedbalance.setHelpInformation(true);
		clearedbalance.setDisabled(true);

		difference = new AmountLabel(messages.Difference());
		difference.setHelpInformation(true);
		difference.setDisabled(true);
		setdataToAmountLables();
		amountsForm.setItems(closebalanceLable, clearedbalance, difference);
		VerticalPanel amountsPanel = new VerticalPanel();
		amountsPanel.setWidth("100%");
		amountsPanel.add(amountsForm);

		amountsPanel.setStyleName("bottom_total_view");
		amountsPanel.setCellHorizontalAlignment(amountsForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		mainPanel.add(bankStatementGrid);
		mainPanel.add(amountsPanel);

		return mainPanel;
	}

	/**
	 * Set amount to non edibles labels
	 */
	private void setdataToAmountLables() {
		double clearedBalance = 0.0;
		double differenceBalance = 0.0;
		differenceBalance = statement.getClosingBalance()
				- statement.getStartBalance();
		for (ClientStatementRecord statementRecord : statement
				.getStatementList()) {
			if (statementRecord.isMatched()) {
				if (statementRecord.getSpentAmount() > 0) {
					clearedBalance -= statementRecord.getSpentAmount();
				} else {
					clearedBalance += statementRecord.getReceivedAmount();
				}
			}
		}
		closebalanceLable.setAmount(statement.getClosingBalance());
		clearedbalance.setAmount(clearedBalance);
		difference.setAmount(differenceBalance - clearedBalance);
	}

	/**
	 * 
	 * @return
	 */
	private VerticalPanel getReconcileTab() {

		VerticalPanel panel = new VerticalPanel();
		main_Vpanel = new VerticalPanel();
		reconcilListGrid = new ReconcilListGrid(account);
		reconcilListGrid.init();
		reconcilListGrid.setCanEdit(true);
		reconcilListGrid.addLoadingImagePanel();
		panel.add(reconcilListGrid);

		int count = 0;
		PaginationList<ClientStatementRecord> list = new PaginationList<ClientStatementRecord>();
		List<ClientStatementRecord> stList = statement.getStatementList();
		Iterator iterator = stList.iterator();
		while ((iterator).hasNext()) {
			ClientStatementRecord obj = (ClientStatementRecord) iterator.next();
			list.add(obj);
			if (obj.isMatched()) {
				count++;
			}
		}
		if (stList.size() == count) {
			saveAndCloseButton.setVisible(true);
		}
		reconcilListGrid.setRecords(list);
		panel.add(main_Vpanel);
		return panel;
	}

	/**
	 * Disable the delete button
	 */
	@Override
	protected boolean canDelete() {
		return false;
	}

	/**
	 * Disable the edit button
	 */
	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientReconciliation());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);
		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ClientReconciliation saveView() {
		ClientReconciliation saveview = super.saveView();
		if (saveview != null) {
			updateObject();
		}
		return super.saveView();
	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());
	}

	/**
 * 
 */
	private void updateObject() {

		data.setAccount(account);
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		Set<ClientReconciliationItem> reconciliationItems = new HashSet<ClientReconciliationItem>();
		for (ClientStatementRecord stRecord : statementList) {
			if (stRecord.isMatched()) {
				List<ClientTransaction> transactionsLists = stRecord
						.getTransactionsLists();
				if (!transactionsLists.isEmpty()) {
					for (ClientTransaction transaction : transactionsLists) {
						ClientReconciliationItem reconciliationItem = new ClientReconciliationItem();
						reconciliationItem.setTransaction(transaction.getID());
						reconciliationItem
								.setTransactionDate(new ClientFinanceDate(
										transaction.getTransactionDate()));
						reconciliationItem.setTransactionNo(transaction
								.getNumber());
						reconciliationItem.setAmount(transaction.getTotal());
						reconciliationItem.setTransationType(transaction
								.getType());
						reconciliationItems.add(reconciliationItem);
					}
				}
			}
		}
		data.setItems(reconciliationItems);
		data.setReconcilationDate(new ClientFinanceDate());
		data.setStartDate(statement.getStartDate());
		data.setEndDate(statement.getEndDate());
		data.setClosingBalance(statement.getClosingBalance());
		data.setOpeningBalance(statement.getStartBalance());
		data.setStatement(statement.getID());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// if statementrecords are not reconciled, show error message
		if (!validateStaementRecords()) {
			result.addError(this.data, "Statement Records are not matched");
		}
		return result;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	private boolean validateStaementRecords() {
		boolean isReconcilable = false;
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		for (ClientStatementRecord record : statementList) {
			// if statementRecord is matched, then set true
			if (record.isMatched()) {
				isReconcilable = true;
			} else {
				// if statementRecord is not matched, return false
				return false;
			}
		}
		return isReconcilable;
	}
}
