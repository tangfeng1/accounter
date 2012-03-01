package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.JobActualCostDetailServerReport;

public class JobActualCostDetailReport extends
		AbstractReportView<JobActualCostDetail> {

	private boolean isActualcostDetail;
	private long customerId;
	private long jobId;

	public JobActualCostDetailReport(boolean isActualcostDetail,
			long customerId, long jobId) {
		this.isActualcostDetail = isActualcostDetail;
		this.customerId = customerId;
		this.jobId = jobId;
		this.serverReport = new JobActualCostDetailServerReport(this,
				isActualcostDetail);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getJobActualCostOrRevenueDetails(start,
				end, isActualcostDetail, customerId, jobId, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void OnRecordClick(JobActualCostDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransaction());
	}

	@Override
	public void print() {
		
		long status = 0;
		if (isActualcostDetail) {
			status = 1;
		}
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 184,
				String.valueOf(customerId), String.valueOf(jobId), status);
		// UIUtils.generateReportPDF(
		// Integer.parseInt(String.valueOf(startDate.getDate())),
		// Integer.parseInt(String.valueOf(endDate.getDate())), 184, "",
		// "", customerName);
	}

	@Override
	public void exportToCsv() {
		// UIUtils.exportReport(
		// Integer.parseInt(String.valueOf(startDate.getDate())),
		// Integer.parseInt(String.valueOf(endDate.getDate())), 184, "",
		// "");
		long status = 0;
		if (isActualcostDetail) {
			status = 1;
		}

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 184,
				String.valueOf(customerId), String.valueOf(jobId), status);
	}
}
