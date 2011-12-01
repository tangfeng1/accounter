package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.server.FinanceTool;

public class UncategorisedVATAmountsReportCommand extends
		NewAbstractReportCommand<UncategorisedAmountsReport> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<UncategorisedAmountsReport>() {

			@Override
			protected String onSelection(UncategorisedAmountsReport selection,
					String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<UncategorisedAmountsReport> records = getRecords();
				ResultList resultList = new ResultList(getMessages()
						.unCategorisedTaxAmountsDetail());
				resultList.setTitle(getMessages()
						.unCategorisedTaxAmountsDetail());
				addSelection(getMessages().unCategorisedTaxAmountsDetail());
				double totalAmount = 0.0;
				for (UncategorisedAmountsReport rec : records) {
					totalAmount += rec.getAmount();
					resultList.add(createReportRecord(rec));
				}
				makeResult.add(resultList);
				makeResult.add(getMessages().unCategorisedTaxAmountsDetail()
						+ " : " + totalAmount);
			}
		});

	}

	protected Record createReportRecord(UncategorisedAmountsReport record) {
		Record uncategoryRecord = new Record(record);
		uncategoryRecord.add(getMessages().transactionType(),
				Utility.getTransactionName(record.getTransactionType()));
		uncategoryRecord.add(getMessages().date(), record.getDate());
		uncategoryRecord.add(getMessages().number(),
				record.getTransactionNumber());
		uncategoryRecord
				.add(getMessages().sourceName(), record.getSourceName());
		uncategoryRecord.add(getMessages().amount(), record.getAmount());
		return uncategoryRecord;
	}

	protected List<UncategorisedAmountsReport> getRecords() {
		ArrayList<UncategorisedAmountsReport> uncategorisedAmountsReports = new ArrayList<UncategorisedAmountsReport>();
		try {
			uncategorisedAmountsReports = new FinanceTool().getReportManager()
					.getUncategorisedAmountsReport(getStartDate(),
							getEndDate(), getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncategorisedAmountsReports;
	}

	protected String addCommandOnRecordClick(
			UncategorisedAmountsReport selection) {
		return "update transaction " + selection.getID();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().uncategorisedVATAmounts());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().uncategorisedVATAmounts());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().uncategorisedVATAmounts());
	}

}