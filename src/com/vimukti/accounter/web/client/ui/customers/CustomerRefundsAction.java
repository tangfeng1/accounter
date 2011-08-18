package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class CustomerRefundsAction extends Action {

	protected CustomerRefundListView view;

	public CustomerRefundsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new CustomerRefundListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerRefundsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customerRefundsList();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/customer_refunds_list.png";
	// }

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "customerRefunds";
	}
}
