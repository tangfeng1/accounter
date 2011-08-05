package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public abstract class BaseView<T extends IAccounterCore> extends
		AbstractBaseView<T> {

	protected ButtonBar buttonBar;

	protected int accountType;

	protected SaveAndCloseButton saveAndCloseButton;

	protected SaveAndNewButtom saveAndNewButton;

	protected CancleButtom cancelButton;

	public BaseView() {
		super();
	}

	protected abstract String getViewTitle();

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
		createView();
		createButtons(getButtonBar());
		this.accountType = getCompany().getAccountingType();

	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void createView() {

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		setWidth("100%");
		setHeight("100%");

		buttonBar = new ButtonBar(this);

		super.add(buttonBar);
	}

	/**
	 * Return list of all DynamicForm items in this view
	 * 
	 * @return
	 */
	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
		// canvas.setHeight(height - 125 + "px");
		// canvas.setWidth(width - 15 + "px");
	}

	public ButtonBar getButtonBar() {
		return this.buttonBar;
	}

	/**
	 * This method will be called my all sub classes to add items to this view.
	 */
	public void add(Widget child) {
		int index = this.getWidgetIndex(buttonBar);
		// Insert widgets above button bar
		super.insert(child, index);
	}


	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndNewButton = new SaveAndNewButtom(this);
		this.cancelButton = new CancleButtom(this);
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(saveAndNewButton);
		buttonBar.add(cancelButton);
	}
}
