package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ImportExcelDataAction extends Action {

	public ImportExcelDataAction(String text) {
		super(text);
	}

	public ImportExcelDataAction(String text, String iconString) {
		super(text, iconString);
	}

//	@Override
//	public ParentCanvas<?> getView() {
//		// NOTHING TO DO.
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "importExcelData";
	}

}
