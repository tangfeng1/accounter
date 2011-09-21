package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VendorCreditMemoView extends
		AbstractVendorTransactionView<ClientVendorCreditMemo> {
	private DynamicForm vendorForm;
	private ArrayList<DynamicForm> listforms;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private boolean locationTrackingEnabled;

	private VendorCreditMemoView() {
		super(ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {

		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientVendorCreditMemo ent = (ClientVendorCreditMemo) this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable.removeAllRecords();
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.removeAllRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		super.vendorSelected(vendor);
		long code = vendor.getTAXCode();
		if (code == 0) {
			code = Accounter.getCompany().getDefaultTaxCode();
		}
		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);

	}

	@Override
	public void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientVendorCreditMemo());
		} else {

			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			transactionNumber.setValue(transaction.getNumber());
			if (isRegistedForVAT()) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		super.initTransactionViewData();

	}

	public void resetElements() {
		this.setVendor(null);
		this.contact = null;
		this.phoneNo = null;
		setMemoTextAreaItem("");
		// setRefText("");

	}

	private boolean isRegistedForVAT() {
		return Accounter.getCompany().getPreferences().isRegisteredForVAT();
	}

	@Override
	public void createControls() {

		Label lab1 = new Label(messages.vendorCredit(Global.get().Vendor())
				+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName(Accounter.constants().labelTitle());
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(messages.vendorCredit(Global.get().Vendor()));

		else
			lab1 = new Label(messages.vendorCredit(Global.get().Vendor()) + "("
					+ getTransactionStatus() + ")");

		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().creditNoteNo());

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);
		VerticalPanel datepanel = new VerticalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));

		contactCombo = createContactComboItem();
		// if (this.isEdit)
		// FIXME--need to disable the form
		// vendorForm.setDisabled(true);

		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);

		DynamicForm phoneForm = UIUtils.form(Accounter.constants()
				.phoneNumber());
		phoneForm.setFields(phoneSelect);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			phoneForm.setFields(classListCombo);
		}
		if (this.isInViewMode()) {
			// FiXME--The form need to be disabled
			// phoneForm.setDisabled(true);
		}

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		// menuButton = createAddNewButton();
		vendorAccountTransactionTable = new VendorAccountTransactionTable() {

			@Override
			protected void updateNonEditableItems() {
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorAccountTransactionTable.getElement().getStyle()
				.setMarginTop(10, Unit.PX);

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel accountFlowPanel = new FlowPanel();
		DisclosurePanel accountsDisclosurePanel = new DisclosurePanel(
				"Itemize by Account");
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");
		vendorItemTransactionTable = new VendorItemTransactionTable() {

			@Override
			protected void updateNonEditableItems() {
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		FlowPanel itemsFlowPanel = new FlowPanel();
		DisclosurePanel itemsDisclosurePanel = new DisclosurePanel(
				"Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");

		vendorForm = UIUtils.form(Global.get().vendor());
		vendorForm.setWidth("50%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect);
		vendorForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "190px");

		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		vatinclusiveCheck = getVATInclusiveCheckBox();
		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() + 100)
		// + "px");

		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);
		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		leftVLay.add(vendorForm);
		VerticalPanel rightVLay1 = new VerticalPanel();
		rightVLay1.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay1.setWidth("100%");
		HorizontalPanel topHLay1 = new HorizontalPanel();
		topHLay1.setWidth("100%");
		topHLay1.add(leftVLay);
		topHLay1.add(rightVLay);

		HorizontalPanel bottomLayout1 = new HorizontalPanel();
		bottomLayout1.setWidth("100%");

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");

		int accountType = getCompany().getAccountingType();
		if (isRegistedForVAT()) {
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.add(totalForm);

			bottomLayout1.add(memoForm);
			bottomLayout1.add(totalForm);
			bottomLayout1.setCellWidth(totalForm, "30%");

			bottomPanel.add(vPanel);
			bottomPanel.add(bottomLayout1);
			//
			// VerticalPanel vPanel = new VerticalPanel();
			//
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout1.add(vPanel);
			// bottomLayout1.add(vatCheckform);
			// bottomLayout1.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout1.add(totalForm);
			// bottomLayout1.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else if (getCompany().getPreferences().isChargeSalesTax()) {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.add(memoForm);
			bottomPanel.add(vPanel);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay1);
		// mainVLay.add(lab2);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		mainVLay.add(bottomPanel);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(vendorForm);

		listforms.add(memoForm);

		listforms.add(vatCheckform);
		listforms.add(totalForm);
		settabIndexes();
	}

	@Override
	public void initMemoAndReference() {

		if (this.isInViewMode()) {

			ClientVendorCreditMemo vendorCreditMemo = (ClientVendorCreditMemo) transaction;

			if (vendorCreditMemo != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(vendorCreditMemo.getMemo());
				// setRefText(vendorCreditMemo.getReference());

			}
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		saveOrUpdate(transaction);

	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Vendor
		if (vendor != null)
			transaction.setVendor(getVendor().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getTotal()
				+ vendorItemTransactionTable.getTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		transaction.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		if (vatinclusiveCheck != null)
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. isBlank transaction?
		// 5. is vendor transaction grid valid?
		if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}
		result.add(vendorForm.validate());
		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					accounterConstants.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		return result;
	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double total = vendorAccountTransactionTable.getTotal()
				+ vendorItemTransactionTable.getTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalNonEditableText.setAmount(total);
		netAmount.setAmount(grandTotal);
		if (isRegistedForVAT()) {
			vatTotalNonEditableText.setAmount(total - grandTotal);
		}
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isVendorCreditMemo()))
	// throw new Exception("Unable to load the Required CashPurchase....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	// }

	public static VendorCreditMemoView getInstance() {

		return new VendorCreditMemoView();

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());

		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		vendorForm.setWidth("40%");
	}

	@Override
	protected String getViewTitle() {
		return messages.vendorCredit(Global.get().Vendor());
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {
		// vendorTransactionTable.refreshAllRecords();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		memoTextAreaItem.setTabIndex(6);
		// menuButton.setTabIndex(7);
		saveAndCloseButton.setTabIndex(8);
		saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

}
