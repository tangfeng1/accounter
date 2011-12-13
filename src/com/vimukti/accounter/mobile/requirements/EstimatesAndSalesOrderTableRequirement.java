package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class EstimatesAndSalesOrderTableRequirement extends
		AbstractTableRequirement<EstimatesAndSalesOrdersList> {

	public EstimatesAndSalesOrderTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, true, true);
		// setDefaultValue(new ArrayList<EstimatesAndSalesOrdersList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getMessages().estimate());
	}

	@Override
	protected void getRequirementsValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected void setRequirementsDefaultValues(EstimatesAndSalesOrdersList obj) {

	}

	@Override
	protected boolean getIsCreatableObject() {
		return true;
	}

	@Override
	protected void addCreateCommands(CommandList commandList) {

	}

	@Override
	public Result run(Context context, Result makeResult, ResultList list,
			ResultList actions) {
		List<EstimatesAndSalesOrdersList> list2 = getList();
		if (!list2.isEmpty()) {
			return super.run(context, makeResult, list, actions);
		} else {
			return null;
		}
	}

	@Override
	protected EstimatesAndSalesOrdersList getNewObject() {
		return new EstimatesAndSalesOrdersList();
	}

	@Override
	protected Record createFullRecord(EstimatesAndSalesOrdersList value) {
		Record rec = new Record(value);
		String name;
		if (value.getType() == ClientTransaction.TYPE_ESTIMATE)
			name = getMessages().quote();
		else
			name = getMessages().salesOrder();

		rec.add(name, value.getTotal());

		return rec;
	}

	@Override
	protected List<EstimatesAndSalesOrdersList> getList() {
		try {
			return new FinanceTool().getCustomerManager()
					.getEstimatesAndSalesOrdersList(getCustomer().getID(),
							getCustomer().getCompany().getID());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return new ArrayList<EstimatesAndSalesOrdersList>();
	}

	protected abstract Customer getCustomer();

	@Override
	protected Record createRecord(EstimatesAndSalesOrdersList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<EstimatesAndSalesOrdersList> oldValues = getValue();
		return (oldValues == null || oldValues.isEmpty()) ? getMessages().addOf(
				getMessages().quotes()) : getMessages().addMore(
				getMessages().quotes());
	}

	@Override
	protected boolean contains(List<EstimatesAndSalesOrdersList> oldValues,
			EstimatesAndSalesOrdersList t) {
		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : oldValues) {
			if (estimatesAndSalesOrdersList.getTransactionId() == t
					.getTransactionId()) {
				return true;
			}
		}
		return false;
	}
}