package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class InvoiceListCommand extends AbstractTransactionCommand {

	AccounterConstants c = getConstants();
	private String CURRENT_VIEW = c.currentView();
	private String FROM_DATE = c.from();
	private String TO_DATE = c.to();
	private String DATE = c.date();
	private String OPEN = c.open();
	private String ALL = c.all();

	private String THIS_WEEK = c.thisWeek();
	private String THIS_MONTH = c.thisMonth();
	private String LAST_WEEK = c.lastWeek();
	private String LAST_MONTH = c.lastMonth();
	private String THIS_FINANCIAL_YEAR = c.thisFinancialYear();
	private String LAST_FINANCIAL_YEAR = c.lastFinancialYear();
	private String THIS_FINANCIAL_QUARTER = c.thisFinancialYear();
	private String lAST_FINANCIAL_QUARTER = c.lastFinancialQuarter();
	private String FINANCIAL_YEAR_TO_DATE = c.financialYearToDate();
	private String CUSTOM = c.custom();
	private int ITEMS_TO_VIEW = 4;
	private int DATE_ITEMS_TO_VIEW = 11;
	private ClientFinanceDate startDate;
	private ClientFinanceDate endDate;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CURRENT_VIEW, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(FROM_DATE, true, true));
		list.add(new Requirement(TO_DATE, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;
		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private void setDefaultValues() {

		get(CURRENT_VIEW).setDefaultValue(OPEN);
		get(DATE).setDefaultValue(ALL);
		get(FROM_DATE).setDefaultValue(new ClientFinanceDate());
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());

	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add(OPEN);
		viewType.add(OVER_DUE);
		viewType.add(VOIDED);
		viewType.add(ALL);

		List<String> dateType = new ArrayList<String>();
		dateType.add(ALL);
		dateType.add(THIS_WEEK);
		dateType.add(THIS_MONTH);
		dateType.add(LAST_WEEK);
		dateType.add(LAST_MONTH);
		dateType.add(THIS_FINANCIAL_YEAR);
		dateType.add(LAST_FINANCIAL_YEAR);
		dateType.add(THIS_FINANCIAL_QUARTER);
		dateType.add(lAST_FINANCIAL_QUARTER);
		dateType.add(FINANCIAL_YEAR_TO_DATE);
		dateType.add(CUSTOM);

		ResultList resultList = new ResultList("values");
		Object selection = context.getSelection(ACTIONS);
		ActionNames actionNames;
		if (selection != null) {
			actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = stringListOptionalRequirement(context, resultList,
				selection, CURRENT_VIEW, CURRENT_VIEW, viewType, getMessages()
						.pleaseSelect(getConstants().currentView()),
				ITEMS_TO_VIEW);
		if (result != null) {
			return result;
		}
		result = stringListOptionalRequirement(context, resultList, selection,
				DATE, c.date(), dateType,
				getMessages().pleaseSelect(getConstants().date()),
				DATE_ITEMS_TO_VIEW);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, resultList, FROM_DATE,
				getConstants().fromDate(),
				getMessages().pleaseSelect(getConstants().fromDate()),
				selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, resultList, TO_DATE,
				getConstants().toDate(),
				getMessages().pleaseSelect(getConstants().toDate()), selection);
		if (result != null) {
			return result;
		}

		String view = get(CURRENT_VIEW).getValue();
		String date = get(DATE).getValue();
		ClientFinanceDate fromDate = get(FROM_DATE).getValue();
		ClientFinanceDate toDate = get(TO_DATE).getValue();

		result = createInvoiceList(context, view, date, fromDate, toDate);
		result.add(resultList);
		return result;

	}

	private Result createInvoiceList(Context context, String viewType,
			String date, ClientFinanceDate startDate, ClientFinanceDate endDate) {
		Result result = context.makeResult();
		ResultList resultList = new ResultList("invoicesList");
		List<InvoicesList> invoices = getInvoices(context.getCompany().getID(),
				viewType, date, startDate, endDate);
		for (InvoicesList invoice : invoices) {
			resultList.add(createInvoiceListRecord(invoice));
		}

		StringBuilder message = new StringBuilder();
		if (resultList.size() > 0) {
			message.append(getMessages().pleaseSelect(
					getConstants().invoiceList()));
		}

		result.add(message.toString());
		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().addaNewInvoice());
		result.add(commandList);
		return result;
	}

	private List<InvoicesList> getInvoices(long companyId, String viewType,
			String dateType, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<InvoicesList> invoices;
		try {

			if (dateType != null) {
				invoices = dateRangeChanged(companyId, dateType);
			} else {
				invoices = new FinanceTool().getInventoryManager()
						.getInvoiceList(companyId, startDate, endDate);
			}
			List<InvoicesList> list = new ArrayList<InvoicesList>(
					invoices.size());
			for (InvoicesList invoice : invoices) {
				if (viewType.equals(OPEN)) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getStatus() != ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}

				} else if (viewType.equals(OVER_DUE)) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getDueDate().compareTo(
									new ClientFinanceDate()) < 0)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(VOIDED)) {
					if (invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(ALL)) {
					list.add(invoice);
				}
			}

			return list;
		} catch (DAOException e) {
		}
		return null;
	}

	public List<InvoicesList> dateRangeChanged(long companyId, String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = getClientCompany().getTransactionStartDate();
		endDate = getClientCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(THIS_WEEK)) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(THIS_MONTH)) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals(LAST_WEEK)) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(LAST_MONTH)) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(THIS_FINANCIAL_YEAR)) {
			startDate = getClientCompany().getCurrentFiscalYearStartDate();
			endDate = getClientCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(LAST_FINANCIAL_YEAR)) {

			startDate = getClientCompany().getCurrentFiscalYearStartDate();
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(getClientCompany().getCurrentFiscalYearEndDate()
					.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals(THIS_FINANCIAL_QUARTER)) {
			startDate = new ClientFinanceDate();
			endDate = getClientCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(lAST_FINANCIAL_QUARTER)) {
			startDate = new ClientFinanceDate();
			endDate = getClientCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(FINANCIAL_YEAR_TO_DATE)) {
			startDate = getClientCompany().getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
		try {
			return new FinanceTool().getInventoryManager().getInvoiceList(
					companyId, startDate, endDate);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Record createInvoiceListRecord(InvoicesList inv) {

		Record record = new Record(inv);

		// record.add("Number", inv.getNumber());
		record.add(c.date(), inv.getDate());
		record.add(c.name(), Utility.getTransactionName((inv.getType())));
		record.add(c.customerName(), inv.getCustomerName());
		// record.add(c.dueDate(), inv.getDueDate());
		// record.add(c.netPrice(), inv.getNetAmount());
		// record.add(c.totalPrice(), inv.getTotalPrice());
		// record.add(c.balance(), inv.getBalance());
		return record;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}
}
