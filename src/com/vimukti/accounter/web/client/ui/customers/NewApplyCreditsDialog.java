package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.CreditsandPaymentsGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class NewApplyCreditsDialog extends BaseDialog<ClientCustomer> {

	AmountField amtDueText, totAmtUseText;

	public CreditsandPaymentsGrid grid;

	private double amountDue;

	public boolean okClicked;

	private List<ClientCreditsAndPayments> totalCredits;

	List<ClientTransactionCreditsAndPayments> initalCreditsUse;

	private ICurrencyProvider currencyProvider;

	private boolean fullyApplied;

	public NewApplyCreditsDialog(List<ClientCreditsAndPayments> totalCredits,
			List<ClientTransactionCreditsAndPayments> usedCredits,
			double amountDue, ICurrencyProvider currencyProvider) {
		super(messages.applyCreditsandPayments());
		this.currencyProvider = currencyProvider;
		this.totalCredits = totalCredits;
		for (ClientCreditsAndPayments ccap : this.totalCredits) {
			double balance = ccap.getBalance();
			double usedAmount = 0.0;
			for (ClientTransactionCreditsAndPayments ctcap : usedCredits) {
				if (ctcap.getCreditsAndPayments() == ccap.getID()) {
					usedAmount += ctcap.getAmountToUse();
				}
			}
			ccap.setRemaoningBalance(balance);
			ccap.setAmtTouse(usedAmount);
		}
		this.initalCreditsUse = usedCredits;
		this.amountDue = amountDue;
		createControls();
		getInitialAmountUse();
		addGridRecordsToListGrid(totalCredits);

	}

	public double getTotalUnuseCreditAmount() {
		double totalUnusedCreditAmount = 0.0;
		List<ClientCreditsAndPayments> records = totalCredits;
		for (ClientCreditsAndPayments crd : records) {
			totalUnusedCreditAmount += crd.getRemaoningBalance()
					+ crd.getAmtTouse();
		}
		return totalUnusedCreditAmount;
	}

	/*
	 * This method invoked when record values are changed.It updates the
	 * non-editable fields
	 */

	/*
	 * This method invoked to set the records to grid and it also updates the
	 * non-editable fileds
	 */
	private void addGridRecordsToListGrid(
			List<ClientCreditsAndPayments> creditsAndPayments) {
		grid.setRecords(creditsAndPayments);
	}

	private void createControls() {
		mainPanel.setSpacing(5);

		amtDueText = new AmountField(messages.amountDue(), this,
				currencyProvider.getTransactionCurrency());
		amtDueText.setColSpan(1);
		amtDueText.setValue(amountAsString(amountDue));
		amtDueText.setDisabled(true);

		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setWidth("100%");
		form.setFields(amtDueText);

		grid = new CreditsandPaymentsGrid(false,
				currencyProvider.getTransactionCurrency());
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("200px");
		grid.setCanEdit(false);
		grid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		totAmtUseText = new AmountField(messages.totalAmountToUse(), this,
				currencyProvider.getTransactionCurrency());
		totAmtUseText.setColSpan(1);
		totAmtUseText.setValue("");
		totAmtUseText.setBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				checkTotalAmount(true);

			}
		});

		okbtn.setTitle(messages.ok());
		cancelBtn.setTitle(messages.close());

		DynamicForm amountUseForm = new DynamicForm();
		amountUseForm.setFields(totAmtUseText);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(form);
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.addStyleName("credits-scroll-panel");
		scrollPanel.add(grid);
		mainVLay.add(scrollPanel);
		mainVLay.add(amountUseForm);

		setBodyLayout(mainVLay);
		setWidth("600px");
		center();
	}

	private void getInitialAmountUse() {
		double amount = 0.0d;
		double totalCreditAmount = getTotalUnuseCreditAmount();
		double appliedCredits = 0.0d;

		if (totalCreditAmount > amountDue) {
			amount = amountDue;
		} else {
			amount = totalCreditAmount;
		}
		if (appliedCredits == 0) {
			totAmtUseText.setAmount(amount);
		} else {
			totAmtUseText.setAmount(appliedCredits);
		}
		amtDueText.setAmount(amountDue);
		if (!totalCredits.isEmpty() && DecimalUtil.isEquals(appliedCredits, 0)) {
			checkTotalAmount(false);
		} else {
			checkTotalAmount(false);
		}
	}

	protected void checkTotalAmount(boolean isBlur) {
		double totalAmount = totAmtUseText.getAmount();
		if (totalAmount > amountDue) {
			Accounter.showError(messages.amountToUseMustLessthanTotal());
			return;
		}
		for (ClientCreditsAndPayments credit : totalCredits) {
			credit.setRemaoningBalance(credit.getRemaoningBalance()
					+ credit.getAmtTouse());
		}
		double amountNeeded = totalAmount;
		for (ClientCreditsAndPayments credit : totalCredits) {
			double balance = credit.getRemaoningBalance();
			double amountToUse = Math.min(amountNeeded, balance);
			credit.setAmtTouse(amountToUse);
			credit.setRemaoningBalance(credit.getRemaoningBalance()
					- amountToUse);
			grid.updateData(credit);
			amountNeeded -= amountToUse;
		}
		fullyApplied = amountNeeded == 0;
	}

	@Override
	public Object getGridColumnValue(ClientCustomer obj, int index) {
		return null;
	}

	@Override
	protected boolean onOK() {
		double totalAmount = totAmtUseText.getAmount();
		if (totalCredits.isEmpty() && totalAmount > 0) {
			Accounter.showError(messages.noCreditsToApply());
			return false;
		} else if (totalAmount > amountDue) {
			Accounter.showError(messages.amountToUseMustLessthanTotal());
			return false;
		} else if (!fullyApplied) {
			Accounter.showError(messages.amountMoreThanCredits());
			return false;
		} else {
			this.initalCreditsUse.clear();
			for (ClientCreditsAndPayments ccap : this.totalCredits) {
				if (ccap.getAmtTouse() > 0) {
					ClientTransactionCreditsAndPayments ctcap = new ClientTransactionCreditsAndPayments();
					ctcap.setAmountToUse(ccap.getAmtTouse());
					ctcap.setCreditsAndPayments(ccap.getID());
					initalCreditsUse.add(ctcap);
				}
				ccap.setBalance(ccap.getRemaoningBalance());
				// }
			}
			return true;
		}

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
