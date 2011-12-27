package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.BudgetCellTable;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class NewBudgetCellTableView extends BaseView<ClientBudget> {

	public static final String AUCTUAL_AMOUNT_LAST_FISCAL_YEAR = Accounter
			.messages().ActualAmountfromlastfiscalyear();
	public static final String AUCTUAL_AMOUNT_THIS_FISCAL_YEAR = Accounter
			.messages().ActualAmountfromthisfiscalyear();
	public static final String NO_AMOUNT = Accounter.messages()
			.StartfromScratch();
	public static final String COPY_FROM_EXISTING = Accounter.messages()
			.CopyfromExistingBudget();

	public static final String SUBDIVIDE_DONT = Accounter.messages()
			.DontSubdevide();
	public static final String SUBDIVIDE_BUSINESS = Accounter.messages()
			.buisiness();
	public static final String SUBDIVIDE_CLASS = Accounter.messages().Class();
	public static final String SUBDIVIDE_CUSTOMER = Accounter.messages()
			.Customer();

	private SelectCombo budgetStartWithSelect, budgetSubdevideBy;
	private SelectCombo selectFinancialYear;
	private TextItem budgetNameText;
	private DynamicForm budgetInfoForm;
	private HorizontalPanel topHLay;
	private HorizontalPanel leftLayout;
	private Label lab1;

	VerticalPanel mainVLay;

	private ArrayList<DynamicForm> listforms;

	List<ClientBudget> budgetList;

	ClientBudget budgetForEditing = new ClientBudget();

	private BudgetCellTable budgetCellTable;
	private boolean isEditing;

	public NewBudgetCellTableView(List<ClientBudget> listData) {
		budgetList = listData;
	}

	public NewBudgetCellTableView() {

	}

	public NewBudgetCellTableView(boolean isEdit, Object data1) {
		isEditing = isEdit;
		data = (ClientBudget) data1;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");

		if (data != null) {
			onEdit();
		}

	}

	@Override
	public void initData() {
		super.initData();
		if (data == null) {
			ClientBudget account = new ClientBudget();
			setData(account);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(Accounter.messages().labelTitle());
		lab1.setText(Accounter.messages().newBudget());

		budgetStartWithSelect = new SelectCombo(Global.get().messages()
				.budgetStartWith());
		budgetStartWithSelect.setHelpInformation(true);
		budgetStartWithSelect.initCombo(getStartWithList());
		budgetStartWithSelect.setSelectedItem(0);
		budgetStartWithSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@SuppressWarnings("unchecked")
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem == COPY_FROM_EXISTING) {
							String budgetTitle = Accounter.messages()
									.CopyvaluesfromExistingBudget();
							CopyBudgetDialogue copybudgetDialogue = new CopyBudgetDialogue(
									budgetTitle, "", budgetList);
							copybudgetDialogue
									.setCallback(new ActionCallback<String>() {

										@Override
										public void actionResult(String result) {
											refreshView(result);

										}

									});
							copybudgetDialogue.show();
						}
					}
				});

		budgetSubdevideBy = new SelectCombo(Global.get().messages()
				.budgetSubdivide());
		budgetSubdevideBy.setHelpInformation(true);
		budgetSubdevideBy.initCombo(getSubdevideList());
		budgetSubdevideBy
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		selectFinancialYear = new SelectCombo(Global.get().messages()
				.budgetFinancialYear());
		selectFinancialYear.setHelpInformation(true);
		selectFinancialYear.initCombo(getFiscalYearList());
		// selectFinancialYear.setSelected(FISCAL_YEAR_3);
		selectFinancialYear.setRequired(true);
		selectFinancialYear
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		budgetNameText = new TextItem(Accounter.messages().payeeName(
				Accounter.messages().budget()));
		budgetNameText.setToolTip(Accounter.messages()
				.giveTheNameAccordingToYourID(this.getAction().getViewName()));
		budgetNameText.setHelpInformation(true);
		budgetNameText.setRequired(true);
		budgetNameText.setWidth(100);
		budgetNameText.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {

				// Converts the first letter of Account Name to Upper case
				String name = budgetNameText.getValue().toString();
				if (name.isEmpty()) {
					return;
				}
				String lower = name.substring(0, 1);
				String upper = lower.toUpperCase();
				budgetNameText.setValue(name.replaceFirst(lower, upper));

			}
		});

		if (data != null) {
			onEditChangeControls();
		}

		budgetInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation());
		budgetInfoForm.setWidth("100%");

		topHLay = new HorizontalPanel();
		topHLay.setWidth("50%");
		leftLayout = new HorizontalPanel();
		leftLayout.setWidth("100%");

		budgetInfoForm.setFields(budgetStartWithSelect, selectFinancialYear,
				budgetNameText);

		leftLayout.add(budgetInfoForm);
		topHLay.add(leftLayout);

		budgetCellTable = new BudgetCellTable();
		if (data != null) {
			budgetCellTable.setDataProvided(data);
		}

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(budgetCellTable);
		budgetCellTable.addColumnSortHandler(new Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				int columnIndex = budgetCellTable
						.getColumnIndex((Column<ClientBudgetItem, ?>) column);
				boolean isAscending = event.isSortAscending();
				budgetCellTable.sortRowData(columnIndex, isAscending);

			}
		});

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(budgetCellTable);
		budgetCellTable.setStyleName("user_activity_log");
		this.add(mainVLay);

		listforms.add(budgetInfoForm);

	}

	private void onEditChangeControls() {

		budgetStartWithSelect.setDisabled(true);
		budgetNameText.setDisabled(true);
		selectFinancialYear.setDisabled(true);
		budgetNameText.setValue(data.getBudgetName());
		selectFinancialYear.setSelected(data.getFinancialYear());
	}

	private List<String> getFiscalYearList() {
		List<String> list = new ArrayList<String>();
		String startingMonth = null;
		String endingMonth = null;

		switch (getCompany().getPreferences().getFiscalYearFirstMonth() + 1) {
		case 1:
			startingMonth = DayAndMonthUtil.jan();
			endingMonth = DayAndMonthUtil.dec();
			break;
		case 2:
			startingMonth = DayAndMonthUtil.feb();
			endingMonth = DayAndMonthUtil.jan();
			break;
		case 3:
			startingMonth = DayAndMonthUtil.mar();
			endingMonth = DayAndMonthUtil.feb();
			break;
		case 4:
			startingMonth = DayAndMonthUtil.apr();
			endingMonth = DayAndMonthUtil.mar();
			break;
		case 5:
			startingMonth = DayAndMonthUtil.mayS();
			endingMonth = DayAndMonthUtil.apr();
			break;
		case 6:
			startingMonth = DayAndMonthUtil.jun();
			endingMonth = DayAndMonthUtil.mayS();
			break;
		case 7:
			startingMonth = DayAndMonthUtil.jul();
			endingMonth = DayAndMonthUtil.jun();
			break;
		case 8:
			startingMonth = DayAndMonthUtil.aug();
			endingMonth = DayAndMonthUtil.jul();
			break;
		case 9:
			startingMonth = DayAndMonthUtil.sep();
			endingMonth = DayAndMonthUtil.aug();
			break;
		case 10:
			startingMonth = DayAndMonthUtil.oct();
			endingMonth = DayAndMonthUtil.sep();
			break;
		case 11:
			startingMonth = DayAndMonthUtil.nov();
			endingMonth = DayAndMonthUtil.oct();
			break;
		case 12:
			startingMonth = DayAndMonthUtil.dec();
			endingMonth = DayAndMonthUtil.nov();
			break;

		default:
			startingMonth = DayAndMonthUtil.jan();
			endingMonth = DayAndMonthUtil.dec();
			break;
		}

		String finalString;

		ClientFinanceDate date = new ClientFinanceDate();
		int year = date.getYear();

		for (long i = year - 10; i < year + 10; i++) {
			finalString = "FY" + Long.toString(i) + "(" + startingMonth
					+ Long.toString(i) + " - " + endingMonth + Long.toString(i)
					+ ")";
			list.add(finalString);
		}
		return list;
	}

	private List<String> getSubdevideList() {
		List<String> list = new ArrayList<String>();

		list.add(SUBDIVIDE_DONT);
		list.add(SUBDIVIDE_BUSINESS);
		list.add(SUBDIVIDE_CLASS);
		list.add(SUBDIVIDE_CUSTOMER);

		return list;
	}

	private List<String> getStartWithList() {
		List<String> list = new ArrayList<String>();

		list.add(NO_AMOUNT);
		list.add(AUCTUAL_AMOUNT_LAST_FISCAL_YEAR);
		list.add(AUCTUAL_AMOUNT_THIS_FISCAL_YEAR);
		list.add(COPY_FROM_EXISTING);

		return list;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Global.get().messages().budget();

	}

	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public ClientBudget saveView() {
		ClientBudget saveView = super.saveView();
		if (data != null) {
			updateBudgetObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateBudgetObject();
		saveOrUpdate(getData());

	}

	private void updateBudgetObject() {

		String budgetname = budgetNameText.getValue() != null ? budgetNameText
				.getValue() : " ";
		data.setBudgetName(budgetname);

		data.setFinancialYear(selectFinancialYear.getSelectedValue());

		List<ClientBudgetItem> allGivenRecords = budgetCellTable.getDataList();
		List<ClientBudgetItem> newList = new ArrayList<ClientBudgetItem>();

		for (ClientBudgetItem clientBudgetItem : allGivenRecords) {

			if (clientBudgetItem.getTotalAmount() > 0) {
				newList.add(clientBudgetItem);
			}
		}

		data.setBudgetItem(newList);

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateBudgetObject();

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
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(budgetInfoForm.validate());
		if (data == null) {
			String name = budgetNameText.getValue().toString() != null ? budgetNameText
					.getValue().toString() : "";

			String financialYear = selectFinancialYear.getSelectedValue();

			String[] temp;
			String delimiter = " ";
			temp = financialYear.split(delimiter);

			String budgetName = name + " - " + temp[0];

			if (name != null && !name.isEmpty()) {

				for (ClientBudget budget : budgetList) {
					if (budgetName.equals(budget.getBudgetName())) {
						result.addError(name, Accounter.messages()
								.alreadyExist());
						break;
					}
				}
			}
		}

		List<ClientBudgetItem> allGivenRecords = budgetCellTable.getDataList();
		List<ClientBudgetItem> newList = new ArrayList<ClientBudgetItem>();
		boolean errorAdded = false;

		for (ClientBudgetItem clientBudgetItem : allGivenRecords) {
			if (clientBudgetItem.getTotalAmount() > 0) {
				errorAdded = true;
				break;
			} else {
				errorAdded = false;
			}
		}
		if (errorAdded == false) {
			result.addError(allGivenRecords, Accounter.messages()
					.noAmountAddedtoAccounts());

		}

		return result;

	}

	private void refreshView(String result) {

	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.BUDGET, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		budgetStartWithSelect.setDisabled(false);
		budgetNameText.setDisabled(false);
		selectFinancialYear.setDisabled(false);

		super.onEdit();

	}

	@Override
	public void setFocus() {
		this.budgetStartWithSelect.setFocus();

	}

}
