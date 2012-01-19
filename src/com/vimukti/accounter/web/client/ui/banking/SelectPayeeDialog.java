package com.vimukti.accounter.web.client.ui.banking;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

public class SelectPayeeDialog extends BaseDialog<ClientPayee> {

	RadioGroupItem typeRadio;

	// private ClientCompany company;
	public SelectPayeeDialog() {
		super(messages.selectPayeeType(), messages.selectOneOfFollowingPayee());

		// company = FinanceApplication.getCompany();
		createControls();
		center();

	}

	private void createControls() {

		mainPanel.setSpacing(15);

		typeRadio = new RadioGroupItem();
		typeRadio.setShowTitle(false);
		typeRadio.setRequired(true);

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();

		typeRadio.setValueMap(messages.customer(), Global.get().Vendor());
		typeRadio.setDefaultValue(messages.customer());

		DynamicForm typeForm = new DynamicForm();
		// typeForm.setIsGroup(true);
		// typeForm.setGroupTitle("Account Type");
		typeForm.setFields(typeRadio);
		typeForm.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(typeForm);

		setBodyLayout(mainVLay);
		setWidth("350px");
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (typeRadio.getValue() == null) {
			result.addError(this, messages.pleaseSelecPaymentType());
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		String radio = typeRadio.getValue().toString();
		// FIXME--an action is required here
		// okClick();
		if (radio.equals(Global.get().Vendor())) {
			// new VendorPaymentsAction("Not Issued").run();
			NewVendorAction action = ActionFactory.getNewVendorAction();
			action.setCallback(new ActionCallback<ClientVendor>() {

				@Override
				public void actionResult(ClientVendor result) {
					setResult(result);
				}
			});

			action.run(null, true);

		} else if (radio.equals(messages.customer())) {
			NewCustomerAction action = ActionFactory.getNewCustomerAction();
			action.setCallback(new ActionCallback<ClientCustomer>() {

				@Override
				public void actionResult(ClientCustomer result) {
					setResult(result);
				}
			});

			action.run(null, true);

		}
		hide();
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
