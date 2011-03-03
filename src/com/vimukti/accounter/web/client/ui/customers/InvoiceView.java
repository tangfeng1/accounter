package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.InvoicePrintLayout;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Fernandez
 * @modified by B.Srinivasa Rao
 * 
 */
public class InvoiceView extends AbstractCustomerTransactionView<ClientInvoice> {

	private InvoiceView() {
		super(ClientTransaction.TYPE_INVOICE, CUSTOMER_TRANSACTION_GRID);

	}

	DateField dueDateItem;
	private Double payments = 0.0;
	private Double balanceDue = 0.0;
	private CustomerQuoteListDialog dialog;
	private LabelItem quoteLabel;
	private String selectedEstimateId;
	private String selectedSalesOrder;
	private ArrayList<DynamicForm> listforms;
	private ArrayList<ClientTransaction> selectedOrdersAndEstimates;
	private TextAreaItem billToTextArea;
	private ShipToForm shipToAddress;
	private TextItem orderNumText;
	@SuppressWarnings("unused")
	private ClientCompany company = FinanceApplication.getCompany();

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();

		initPaymentTerms();

		initShippingTerms();

		initShippingMethod();

		initDueDate();

		initPayments();

		initBalanceDue();

	}

	private void initBalanceDue() {

		if (transactionObject != null) {

			setBalanceDue(((ClientInvoice) transactionObject).getBalanceDue());

		}

	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPayments() {

		if (transactionObject != null) {

			ClientInvoice invoice = (ClientInvoice) transactionObject;

			setPayments(invoice.getPayments());
		}

	}

	@Override
	protected void initPaymentTerms() {

		paymentTermsList = FinanceApplication.getCompany().getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);
		for (ClientPaymentTerms paymentTerm : paymentTermsList) {
			if (paymentTerm.getName().equals("Due on Receipt")) {
				payTermsSelect.addItemThenfireEvent(paymentTerm);
				break;
			}
		}
		this.paymentTerm = payTermsSelect.getSelectedValue();
	}

	private void initDueDate() {

		if (transactionObject != null) {
			ClientInvoice invoice = (ClientInvoice) transactionObject;
			if (invoice.getDueDate() != 0) {
				dueDateItem.setEnteredDate(new ClientFinanceDate(invoice
						.getDueDate()));
			} else if (invoice.getPaymentTerm() != null) {
				ClientPaymentTerms terms = FinanceApplication.getCompany()
						.getPaymentTerms(invoice.getPaymentTerm());
				ClientFinanceDate transactionDate = this.transactionDateItem
						.getEnteredDate();
				ClientFinanceDate dueDate = new ClientFinanceDate(invoice
						.getDueDate());
				dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
				if (dueDate != null) {
					dueDateItem.setEnteredDate(dueDate);
				}

			}

		} else
			dueDateItem.setEnteredDate(new ClientFinanceDate());

	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(customerConstants.newInvoice()));
		Label lab1;

		if (transactionObject == null
				|| transactionObject.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(FinanceApplication.getCustomersMessages()
					.invoice());

		else {
			// lab1 = new Label("Invoice(" + getTransactionStatus() + ")");
			lab1 = new Label(FinanceApplication.getCustomersMessages()
					.invoice());
		}

		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						setDateValues(date);
					}
				});
		transactionDateItem.setHelpInformation(true);
		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(FinanceApplication.getCustomersMessages()
				.invoiceNo());
		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(customerConstants
				.customerName());
		customerCombo.setHelpInformation(true);
		customerCombo.setWidth("100%");
		quoteLabel = new LabelItem();
		quoteLabel.setValue(FinanceApplication.getCustomersMessages()
				.quotesandsalesOrder());
		quoteLabel.setWidth("100%");
		quoteLabel.addStyleName("falseHyperlink");
		quoteLabel.setShowTitle(false);
		quoteLabel.setDisabled(isEdit);
		LabelItem emptylabel = new LabelItem();
		emptylabel.setValue("");
		emptylabel.setWidth("100%");
		emptylabel.setShowTitle(false);

		quoteLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getEstimatesAndSalesOrder();
			}
		});

		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// billToCombo = createBillToComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setHelpInformation(true);
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(FinanceApplication.getCustomersMessages()
				.billTo());
		billToTextArea.setDisabled(true);
		billToTextArea.setHelpInformation(true);

		shipToCombo = createShipToComboItem();
		shipToCombo.setHelpInformation(true);
		shipToAddress = new ShipToForm(null);
		shipToAddress.businessSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				shippingAddress = shipToAddress.getAddress();
				if (shippingAddress != null)
					shipToAddress.setAddres(shippingAddress);
				else
					shipToAddress.addrArea.setValue("");
			}
		});

		if (transactionObject != null)
			shipToAddress.businessSelect.setDisabled(true);
		// phoneSelect = new SelectItem();
		// phoneSelect.setWidth(100);
		// phoneSelect.setTitle(customerConstants.phone());
		// phoneSelect.setDisabled(isEdit);
		// phoneSelect.addChangeHandler(new ChangeHandler() {
		//
		// public void onChange(ChangeEvent event) {
		//
		// phoneNo = phoneSelect.getValue().toString();
		//
		// }
		// });
		custForm = UIUtils.form(customerConstants.customer());
		custForm.setNumCols(3);
		custForm.setWidth("100%");
		forms.add(custForm);
		custForm.setFields(customerCombo, quoteLabel, contactCombo, emptylabel,
				billToTextArea, emptylabel);
		custForm.setStyleName("align-form");

		if (UIUtils.isMSIEBrowser()) {
			if (transactionObject != null)
				custForm.setWidth("74%");
		}

		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(customerConstants.dueDate());
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(customerConstants.dueDate());
		dueDateItem.setDisabled(isEdit);
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		orderNumText = new TextItem(FinanceApplication.getCustomersMessages()
				.salesorderno());
		orderNumText.setHelpInformation(true);
		orderNumText.setWidth(38);
		if (transactionObject != null)
			orderNumText.setDisabled(true);

		DynamicForm termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setGroupTitle(customerConstants.terms());
		termsForm.setNumCols(2);
		termsForm.setFields(salesPersonCombo, payTermsSelect,
				shippingTermsCombo, shippingMethodsCombo, dueDateItem,
				deliveryDate, orderNumText);
		termsForm.setStyleName("align-form");
		forms.add(termsForm);

		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		Button printButton = new Button();
		printButton.setText(FinanceApplication.getCustomersMessages().print());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				InvoicePrintLayout printLt = new InvoicePrintLayout(
						(ClientInvoice) getInvoiceObject());
				printLt.setView(InvoiceView.this);
				printLt.createTemplate();
				printLt.print();
			}
		});

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setFields(memoTextAreaItem);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(createAddNewButton());
		vPanel.add(prodAndServiceForm1);
		vPanel.setWidth("100%");
		forms.add(prodAndServiceForm1);

		priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		netAmountLabel = createNetAmountLabel();

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		paymentsNonEditableText = new AmountLabel(customerConstants.payments());
		paymentsNonEditableText.setDisabled(true);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		balanceDueNonEditableText = new AmountLabel(customerConstants
				.balanceDue());
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("50%");
		prodAndServiceForm2.setNumCols(4);
		prodAndServiceForm2.setCellSpacing(5);
		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		prodAndServiceHLay.add(vPanel);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {

			DynamicForm priceLevelForm = new DynamicForm();
			// priceLevelForm.setCellSpacing(4);
			priceLevelForm.setWidth("70%");
			priceLevelForm.setFields(priceLevelSelect);
			DynamicForm amountsForm = new DynamicForm();
			amountsForm.setWidth("50%");
			amountsForm.setFields(netAmountLabel, vatTotalNonEditableText,
					transactionTotalNonEditableText, paymentsNonEditableText,
					balanceDueNonEditableText);
			// forms.add(priceLevelForm);
			forms.add(amountsForm);
			// prodAndServiceHLay.add(priceLevelForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm,
			// ALIGN_RIGHT);
			prodAndServiceHLay.add(amountsForm);
			prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
					ALIGN_RIGHT);

			// listforms.add(priceLevelForm);

		} else {

			// prodAndServiceForm2.setFields(salesTaxTextNonEditable,
			// transactionTotalNonEditableText, paymentsNonEditableText,
			// balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);
			prodAndServiceForm2.setFields(salesTaxTextNonEditable,
					transactionTotalNonEditableText, paymentsNonEditableText,
					balanceDueNonEditableText, taxCodeSelect);

			forms.add(prodAndServiceForm2);

			prodAndServiceHLay.add(prodAndServiceForm2);
			prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
					ALIGN_RIGHT);
		}

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setSpacing(10);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(customerTransactionGrid);

		mainVLay.add(prodAndServiceHLay);

		if (UIUtils.isMSIEBrowser())
			resetFromView();

		canvas.add(mainVLay);

	}

	public AbstractTransactionGrid<ClientTransactionItem> getGridForPrinting() {
		return customerTransactionGrid;
	}

	protected void setDateValues(ClientFinanceDate date) {
		if (date != null) {
			deliveryDate.setEnteredDate(date);
			dueDateItem.setValue(date);
			setTransactionDate(date);
			calculateDatesforPayterm(date);
			updateNonEditableItems();
		}

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(FinanceApplication.getCompany()
					.getPriceLevel(priceLevel.getStringID()));

		}
		if (this.transactionObject == null || customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	@Override
	public void updateNonEditableItems() {

		if (customerTransactionGrid == null)
			return;
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;

			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					FinanceApplication.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);
		} else {
			if (customerTransactionGrid.getGrandTotal() != null
					&& customerTransactionGrid.getTotalValue() != null) {
				netAmountLabel.setAmount(customerTransactionGrid
						.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}
		Double payments = this.paymentsNonEditableText.getAmount();
		if (transactionObject != null) {
			payments = this.transactionTotal < payments ? this.transactionTotal
					: payments;
			setPayments(payments);
		}
		setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {

		updateSalesOrderOrEstimate(customer);

		if (this.customer != null && !this.customer.equals(customer)
				&& transactionObject == null)
			customerTransactionGrid.removeAllRecords();

		this.customer = customer;
		super.customerSelected(customer);
		selectedOrdersAndEstimates = new ArrayList<ClientTransaction>();

		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(customer);
		}
		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setCustomerTaxCodetoAccount();
	}

	/**
	 * Update sales orders and estimates when customer has been changed during
	 * edit of transactions. if customer changed remove present records in grid
	 * or if customer revert to old customer.than Reinitialise records in grid
	 * again.
	 * 
	 * @param customer
	 */
	private void updateSalesOrderOrEstimate(ClientCustomer customer) {
		if (this.customer != null && this.customer != customer) {
			ClientInvoice inv = (ClientInvoice) this.transactionObject;

			if (inv != null && inv.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(inv
						.getTransactionItems());
				selectedSalesOrder = inv.getSalesOrder();
				selectedEstimateId = inv.getEstimate();
			} else if (inv != null
					&& !inv.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				selectedSalesOrder = "nostringid";
				selectedEstimateId = "nostringid";
			}
		}

	}

	protected void showQuotesDialog(List<EstimatesAndSalesOrdersList> result) {
		// if (result == null)
		// return;

		List<EstimatesAndSalesOrdersList> filteredList = new ArrayList<EstimatesAndSalesOrdersList>();
		filteredList.addAll(result);

		for (EstimatesAndSalesOrdersList record : result) {
			if (selectedOrdersAndEstimates != null)
				for (ClientTransaction transaction : selectedOrdersAndEstimates) {
					if (transaction.getStringID().equals(
							record.getTransactionId()))
						filteredList.remove(record);
				}
		}
		if (dialog == null) {
			dialog = new CustomerQuoteListDialog(this, filteredList);
		}

		dialog.setQuoteList(filteredList);
		dialog.show();

		if (filteredList.isEmpty()) {
			dialog.grid.addEmptyMessage("No records to show");
		}

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(FinanceApplication.getCompany()
					.getSalesPerson(salesPerson.getStringID()));

		}
		salesPersonCombo.setDisabled(isEdit);
	}

	public void selectedQuote(ClientEstimate selectedEstimate) {
		if (selectedEstimate == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : selectedEstimate
					.getTransactionItems())
				if (record.getReferringTransactionItem().equals(
						salesRecord.getStringID()))
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedOrdersAndEstimates != null)
			selectedOrdersAndEstimates.add(selectedEstimate);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : selectedEstimate
				.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				// clientItem.setVatCode(item.getTaxCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getStringID());
				itemsList.add(clientItem);
			}

		}
		selectedEstimateId = selectedEstimate.getStringID();
		orderNum = selectedEstimate.getNumber();
		orderNumText.setValue(orderNum);
		customerTransactionGrid.setAllTransactions(itemsList);

		// if (selectedEstimate == null)
		// return;
		// if (selectedOrdersAndEstimates != null)
		// selectedOrdersAndEstimates.add(selectedEstimate);
		//
		// ClientInvoice convertedIinvoice = convertToInvoice(selectedEstimate);
		//
		// selectedEstimateId = selectedEstimate.getStringID();
		//
		// if (convertedIinvoice == null) {
		// Accounter.showError("Could Not Load the Quote....");
		// return;
		// }
		//
		// // initTransactionViewData(convertedIinvoice);
		// this.transactionItems = convertedIinvoice.getTransactionItems();
		// customerTransactionGrid.setAllTransactions(transactionItems);
		// // customerTransactionGrid.updateData(obj)

	}

	@SuppressWarnings("unused")
	private ClientInvoice convertToInvoice(ClientEstimate selectedEstimate) {

		ClientInvoice invoice = new ClientInvoice(selectedEstimate);
		setShippingAdress(invoice);
		for (ClientTransactionItem item : invoice.getTransactionItems()) {
			item.setStringID("");
		}

		return invoice;
	}

	private void setShippingAdress(ClientInvoice invoice) {
		ClientCustomer customer = FinanceApplication.getCompany().getCustomer(
				invoice.getCustomer());
		this.addressListOfCustomer = customer.getAddress();
		ClientAddress shippingAdressValue = getAddress(ClientAddress.TYPE_SHIP_TO);
		invoice.setShippingAdress(shippingAdressValue);

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		initTransactionViewData();
		ClientInvoice invoiceToBeEdited = (ClientInvoice) transactionObject;
		ClientCompany company = FinanceApplication.getCompany();
		this.customer = company.getCustomer(invoiceToBeEdited.getCustomer());
		this.contact = invoiceToBeEdited.getContact();
		// customerSelected(company.getCustomer(invoiceToBeEdited.getCustomer()));

		if (invoiceToBeEdited.getPhone() != null)
			this.phoneNo = invoiceToBeEdited.getPhone();
		// phoneSelect.setValue(this.phoneNo);
		this.billingAddress = invoiceToBeEdited.getBillingAddress();
		this.shippingAddress = invoiceToBeEdited.getShippingAdress();
		this.transactionItems = invoiceToBeEdited.getTransactionItems();
		this.priceLevel = company.getPriceLevel(invoiceToBeEdited
				.getPriceLevel());
		this.payments = invoiceToBeEdited.getPayments();
		this.salesPerson = company.getSalesPerson(invoiceToBeEdited
				.getSalesPerson());
		this.shippingMethod = company.getShippingMethod(invoiceToBeEdited
				.getShippingMethod());
		this.paymentTerm = company.getPaymentTerms(invoiceToBeEdited
				.getPaymentTerm());
		this.shippingTerm = company.getShippingTerms(invoiceToBeEdited
				.getShippingTerm());
		initTransactionNumber();

		this.orderNumText
				.setValue(invoiceToBeEdited.getOrderNum() != null ? invoiceToBeEdited
						.getOrderNum()
						: "");
		// this.taxCode =
		// getTaxItemGroupForTransactionItems(this.transactionItems);
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(customer);
		}

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		if (customer != null)
			addresses.addAll(customer.getAddress());
		shipToAddress.setListOfCustomerAdress(addresses);
		if (shippingAddress != null) {
			shipToAddress.businessSelect.setValue(shippingAddress
					.getAddressTypes().get(shippingAddress.getType() + ""));
			shipToAddress.setAddres(shippingAddress);
		}

		this.addressListOfCustomer = customer.getAddress();

		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");
		// billToaddressSelected(this.billingAddress);
		// shipToAddressSelected(this.shippingAddress);
		contactSelected(this.contact);
		paymentTermsSelected(this.paymentTerm);
		priceLevelSelected(this.priceLevel);
		salesPersonSelected(this.salesPerson);
		shippingMethodSelected(this.shippingMethod);
		shippingTermSelected(this.shippingTerm);
		taxCodeSelected(this.taxCode);
		if (invoiceToBeEdited.getMemo() != null)
			memoTextAreaItem.setValue(invoiceToBeEdited.getMemo());
		// if (invoiceToBeEdited.getReference() != null)
		// refText.setValue(invoiceToBeEdited.getReference());

		if (invoiceToBeEdited.getDeliverydate() != 0)
			this.deliveryDate.setValue(new ClientFinanceDate(invoiceToBeEdited
					.getDeliverydate()));
		this.dueDateItem
				.setValue(invoiceToBeEdited.getDueDate() != 0 ? new ClientFinanceDate(
						invoiceToBeEdited.getDueDate())
						: getTransactionDate());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(invoiceToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(invoiceToBeEdited.getTotal()
					- invoiceToBeEdited.getNetAmount());
			// vatinclusiveCheck.setValue(invoiceToBeEdited.isAmountsIncludeVAT());
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}
			this.salesTaxTextNonEditable.setValue(invoiceToBeEdited
					.getSalesTaxAmount());
		}

		transactionTotalNonEditableText.setAmount(invoiceToBeEdited.getTotal());
		paymentsNonEditableText.setAmount(invoiceToBeEdited.getPayments());
		balanceDueNonEditableText.setAmount(invoiceToBeEdited.getBalanceDue());
		quoteLabel.setDisabled(true);
		customerTransactionGrid.setCanEdit(false);

	}

	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToAddress != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientInvoice) transactionObject)
					.getSalesTaxAmount();
			setSalesTax(salesTaxAmout);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientInvoice) transactionObject)
					.getTotal();
			setTransactionTotal(transactionTotal);

		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientInvoice invoice = (ClientInvoice) transactionObject;

			if (invoice != null) {

				memoTextAreaItem.setValue(invoice.getMemo());
				// refText.setValue(invoice.getReference());

			}

		}

	}

	@Override
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(FinanceApplication.getCompany()
					.getPaymentTerms(paymentTerm.getStringID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		calculateDatesforPayterm(transDate);
	}

	private void calculateDatesforPayterm(ClientFinanceDate transDate) {
		if (transDate != null && this.paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					this.paymentTerm);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		try {
			transactionObject = getInvoiceObject();
			ClientInvoice invoice = transactionObject != null ? (ClientInvoice) transactionObject
					: new ClientInvoice();
			invoice.setCustomer(customer.getStringID());

			if (dueDateItem.getEnteredDate() != null)
				invoice.setDueDate((dueDateItem.getEnteredDate()).getTime());
			if (deliveryDate.getEnteredDate() != null)
				invoice
						.setDeliverydate(deliveryDate.getEnteredDate()
								.getTime());
			if (FinanceApplication.getCompany().getAccountingType() == 0)
				invoice.setSalesTaxAmount(salesTaxTextNonEditable.getAmount());

			if (contactCombo.getSelectedValue() != null) {
				contact = contactCombo.getSelectedValue();
				invoice.setContact(contact);
			}
			if (phoneNo != null)
				invoice.setPhone(phoneNo);
			if (billingAddress != null)
				invoice.setBillingAddress(billingAddress);
			if (shippingAddress != null)
				invoice.setShippingAdress(shippingAddress);
			if (salesPerson != null)
				invoice.setSalesPerson(salesPerson.getStringID());
			if (paymentTerm != null)
				invoice.setPaymentTerm(paymentTerm.getStringID());
			if (shippingTerm != null)
				invoice.setShippingTerm(shippingTerm.getStringID());
			if (shippingMethod != null)
				invoice.setShippingMethod(shippingMethod.getStringID());
			if (priceLevel != null)
				invoice.setPriceLevel(priceLevel.getStringID());

			if (orderNumText.getValue() != null
					&& !orderNumText.getValue().equals(""))
				orderNum = orderNumText.getValue().toString();

			if (orderNum != null)
				invoice.setOrderNum(orderNum);
			// if (taxItemGroup != null)
			// invoice.setTaxItemGroup(taxItemGoup);
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				if (taxCode != null) {
					for (ClientTransactionItem record : customerTransactionGrid
							.getRecords()) {
						record.setTaxCode(taxCode.getStringID());

					}
				}
				invoice.setSalesTaxAmount(this.salesTax);
			} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				invoice.setNetAmount(netAmountLabel.getAmount());
				invoice.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			}
			invoice.setTotal(transactionTotalNonEditableText.getAmount());
			// invoice.setBalanceDue(getBalanceDue());
			invoice.setPayments(getPayments());
			invoice.setMemo(getMemoTextAreaItem());
			// invoice.setReference(getRefText());

			ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
					transactionDateItem.getEnteredDate(), paymentTerm);
			invoice.setDiscountDate(discountDate.getTime());

			if (selectedEstimateId != null)
				invoice.setEstimate(selectedEstimateId);
			if (selectedSalesOrder != null)
				invoice.setSalesOrder(selectedSalesOrder);

			if (customerTransactionGrid != null)
				// invoice.setTotal(transactionGrid.getTotal());

				transactionObject = invoice;

			super.saveAndUpdateView();
			if (transactionObject.getStringID() != null) {
				alterObject(invoice);

			} else {
				createObject(invoice);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	private ClientTransaction getInvoiceObject() {
		ClientInvoice invoice = transactionObject != null ? (ClientInvoice) transactionObject
				: new ClientInvoice();
		if (customer != null)
			invoice.setCustomer(customer.getStringID());

		if (dueDateItem.getEnteredDate() != null)
			invoice.setDueDate((dueDateItem.getEnteredDate()).getTime());
		if (deliveryDate.getEnteredDate() != null)
			invoice.setDeliverydate(deliveryDate.getEnteredDate().getTime());
		if (FinanceApplication.getCompany().getAccountingType() == 0)
			invoice.setSalesTaxAmount(salesTaxTextNonEditable.getAmount());
		if (contact != null)
			invoice.setContact(contact);
		if (phoneNo != null)
			invoice.setPhone(phoneNo);
		if (billingAddress != null)
			invoice.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			invoice.setShippingAdress(shippingAddress);
		if (salesPerson != null)
			invoice.setSalesPerson(salesPerson.getStringID());
		if (paymentTerm != null)
			invoice.setPaymentTerm(paymentTerm.getStringID());
		if (shippingTerm != null)
			invoice.setShippingTerm(shippingTerm.getStringID());
		if (shippingMethod != null)
			invoice.setShippingMethod(shippingMethod.getStringID());
		if (priceLevel != null)
			invoice.setPriceLevel(priceLevel.getStringID());

		if (orderNumText.getValue() != null
				&& !orderNumText.getValue().equals(""))
			orderNum = orderNumText.getValue().toString();

		if (orderNum != null)
			invoice.setOrderNum(orderNum);
		// if (taxItemGroup != null)
		// invoice.setTaxItemGroup(taxItemGroup);

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			if (taxCode != null) {
				for (ClientTransactionItem record : customerTransactionGrid
						.getRecords()) {
					record.setTaxItemGroup(taxCode.getStringID());

				}
			}
			invoice.setSalesTaxAmount(this.salesTax);
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			invoice.setNetAmount(netAmountLabel.getAmount());
			invoice
					.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
							.getValue());
		}
		invoice.setTotal(transactionTotalNonEditableText.getAmount());
		// invoice.setBalanceDue(getBalanceDue());
		invoice.setPayments(getPayments());
		invoice.setMemo(getMemoTextAreaItem());
		// invoice.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), paymentTerm);
		invoice.setDiscountDate(discountDate.getTime());

		if (selectedEstimateId != null)
			invoice.setEstimate(selectedEstimateId);
		if (selectedSalesOrder != null)
			invoice.setSalesOrder(selectedSalesOrder);

		if (customerTransactionGrid != null)
			// invoice.setTotal(transactionGrid.getTotal());
			transactionObject = invoice;
		try {
			super.saveAndUpdateView();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionObject;
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		ClientCustomer previousCustomer = customer;
		if (customer != null && customer != previousCustomer)
			getEstimatesAndSalesOrder();
		return super.validate();

	}

	public void setPayments(Double payments) {
		if (payments == null)
			payments = 0.0D;
		this.payments = payments;
		paymentsNonEditableText.setAmount(payments);
	}

	public Double getPayments() {
		return payments;
	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public static InvoiceView getInstance() {

		return new InvoiceView();
	}

	private void getEstimatesAndSalesOrder() {
		if (this.rpcUtilService == null)
			return;
		if (customer == null) {
			Accounter.showError(FinanceApplication.getCustomersMessages()
					.pleaseSelectCustomer());
		} else {

			// if (dialog != null && dialog.preCustomer != null
			// && dialog.preCustomer == this.customer) {
			// return;
			// }
			AsyncCallback<List<EstimatesAndSalesOrdersList>> callback = new AsyncCallback<List<EstimatesAndSalesOrdersList>>() {

				@Override
				public void onFailure(Throwable caught) {
					// Accounter.showError(FinanceApplication
					// .getCustomersMessages()
					// .noQuotesAndSalesOrderForCustomer()
					// + " " + customer.getName());
					return;
				}

				@Override
				public void onSuccess(List<EstimatesAndSalesOrdersList> result) {
					if (result == null)
						onFailure(new Exception());

					if (result.size() > 0) {
						showQuotesDialog(result);
					} else {
						showQuotesDialog(result);
					}

				}

			};
			this.rpcUtilService.getEstimatesAndSalesOrdersList(customer
					.getStringID(), callback);

		}
	}

	public void selectedSalesOrder(ClientSalesOrder salesOrder) {
		// this.transactionItems = salesOrder.getTransactionItems();
		if (salesOrder == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : salesOrder
					.getTransactionItems())
				if (record.getReferringTransactionItem().equals(
						salesRecord.getStringID()))
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedOrdersAndEstimates != null)
			selectedOrdersAndEstimates.add(salesOrder);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : salesOrder.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				// clientItem.setVatCode(item.getTaxCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getStringID());
				itemsList.add(clientItem);
			}

		}
		selectedSalesOrder = salesOrder.getStringID();
		orderNum = salesOrder.getNumber();
		orderNumText.setValue(orderNum);
		customerTransactionGrid.setAllTransactions(itemsList);
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.customerCombo.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {

		if (UIUtils.isMSIEBrowser())
			custForm.setWidth("100%");

		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);

		customerCombo.setDisabled(isEdit);
		quoteLabel.setDisabled(isEdit);

		shipToAddress.businessSelect.setDisabled(isEdit);

		salesPersonCombo.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);

		dueDateItem.setDisabled(isEdit);
		deliveryDate.setDisabled(isEdit);

		priceLevelSelect.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);

		orderNumText.setDisabled(isEdit);

		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();

	}

	@Override
	public void print() {
		UIUtils.downloadAttachment(((ClientInvoice) getInvoiceObject())
				.getStringID(), ClientTransaction.TYPE_INVOICE);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.addComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.addComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.addComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.updateComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.updateComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.updateComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.removeComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.removeComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			break;
		}

	}

	private void resetFromView() {
		custForm.getCellFormatter().setWidth(0, 1, "200");

		shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		shipToAddress.getCellFormatter().setWidth(0, 2, "200");

		priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
	}

	@Override
	public void printPreview() {

	}

	private String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(FinanceApplication.getCompany()
					.getTAXCode(taxCode.getStringID()));
			customerTransactionGrid.setTaxCode(taxCode.getStringID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

}
