package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.company.PreferencePage;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PreferenceSettingsView extends BaseView<ClientCompanyPreferences> {

	private ScrollPanel pageDetailsPanel;

	private List<PreferencePage> preferencePages;

	private ArrayList<StyledPanel> subTabsPanels;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("PreferenceSettingsView");
		createControls();
	}

	private void createControls() {
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		StyledPanel titlesPanel = new StyledPanel("titlesPanel");
		pageDetailsPanel = new ScrollPanel();
		pageDetailsPanel.addStyleName("pre_scroll_table");

		preferencePages = new ArrayList<PreferencePage>();

		final List<String> preferencePagesTitle = getPreferencePagesTitle();
		final List<Label> titles = new ArrayList<Label>();
		subTabsPanels = new ArrayList<StyledPanel>();

		for (int i = 0; i < preferencePagesTitle.size(); i++) {
			final Label title = new Label(preferencePagesTitle.get(i));
			title.addStyleName("preferences_option_title");
			StyledPanel panel = new StyledPanel("Sub-tabs-panel");

			titles.add(title);
			titlesPanel.add(titles.get(i));

			subTabsPanels.add(panel);
			titlesPanel.add(subTabsPanels.get(i));

			title.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int j = 0;
					for (int k = 0; k < preferencePagesTitle.size(); k++) {
						subTabsPanels.get(k).clear();
						if (titles.get(k).equals(title)) {
							j = k;
						}
					}
					Label source = (Label) event.getSource();
					String text = source.getText();
					createPage(text, j);
				}
			});

		}
		createPage(preferencePagesTitle.get(0), 0);
		mainPanel.add(titlesPanel);
		mainPanel.add(pageDetailsPanel);
		titlesPanel.getElement().getParentElement()
				.addClassName("titles_panel_td");
		pageDetailsPanel.getElement().getParentElement()
				.addClassName("page_details_panel_td");
		mainPanel.addStyleName("company_settings_panel");
		this.add(mainPanel);
		// setSize("100%", "100%");
	}

	protected void createPage(String text, int j) {
		pageDetailsPanel.clear();

		PreferencePage page = null;

		for (PreferencePage widget : preferencePages) {
			String title = widget.getTitle();
			if (text.equalsIgnoreCase(title) || text.equalsIgnoreCase(title)
					|| text.equalsIgnoreCase(title)
					|| text.equalsIgnoreCase(title)) {
				page = widget;
				break;
			}
		}

		if (page == null && text.equalsIgnoreCase(messages.company())) {
			page = getCompanyInfoPage();
			preferencePages.add(page);
		} else if (page == null && text.equalsIgnoreCase(messages.categories())) {
			page = getCatogiriesInfoPage();
			preferencePages.add(page);
		} else if (page == null
				&& text.equalsIgnoreCase(messages.vendorAndPurchases(Global
						.get().Vendor()))) {
			page = getVendorAndPurchasesPage();
			preferencePages.add(page);
		} else if (page == null
				&& text.equalsIgnoreCase(messages.customersAndSales(Global
						.get().Customer()))) {

			page = getCustomerAndSalesPage();
			preferencePages.add(page);
		}

		subTabsPanels.get(j).add(createPageView(page));
		pageDetailsPanel.add(page);

	}

	private List<String> getPreferencePagesTitle() {
		List<String> title = new ArrayList<String>();
		title.add(messages.company());
		if (hasPermission(Features.LOCATION) || hasPermission(Features.CLASS)
				|| hasPermission(Features.JOB_COSTING)) {
			title.add(messages.categories());
		}
		title.add(messages.vendorAndPurchases(Global.get().Vendor()));
		title.add(messages.customersAndSales(Global.get().Customer()));

		return title;
	}

	private List<PreferencePage> getPreferencePages() {
		List<PreferencePage> preferenceList = new ArrayList<PreferencePage>();
		preferenceList.add(getCompanyInfoPage());
		if (hasPermission(Features.LOCATION) || hasPermission(Features.CLASS)
				|| hasPermission(Features.JOB_COSTING)) {
			preferenceList.add(getCatogiriesInfoPage());
		}
		preferenceList.add(getVendorAndPurchasesPage());
		preferenceList.add(getCustomerAndSalesPage());
		return preferenceList;
	}

	private PreferencePage getCustomerAndSalesPage() {
		PreferencePage customerAndSalesPage = new PreferencePage(
				messages.customersAndSales(Global.get().Customer()));
		AgeingAndSellingDetailsOption ageingAndSellingDetailsOption = new AgeingAndSellingDetailsOption();
		ProductAndServicesOption productAndServicesOption = new ProductAndServicesOption();
		TrackDiscountsOption discountsOption = new TrackDiscountsOption();
		TrackEstimatesOption estimatesOption = new TrackEstimatesOption();
		DoyouUseShipingsOption shipingsOption = new DoyouUseShipingsOption();

		customerAndSalesPage.addPreferenceOption(productAndServicesOption);
		customerAndSalesPage.addPreferenceOption(ageingAndSellingDetailsOption);
		customerAndSalesPage.addPreferenceOption(discountsOption);
		customerAndSalesPage.addPreferenceOption(estimatesOption);
		customerAndSalesPage.addPreferenceOption(shipingsOption);

		return customerAndSalesPage;
	}

	private PreferencePage getVendorAndPurchasesPage() {
		PreferencePage vendorsAndPurchasesPage = new PreferencePage(
				messages.vendorAndPurchases(Global.get().Vendor()));

		ManageBillsOption manageBillsOption = new ManageBillsOption();
		ExpensesByCustomerOption expensesByCustomerOption = new ExpensesByCustomerOption();

		vendorsAndPurchasesPage.addPreferenceOption(manageBillsOption);
		if (hasPermission(Features.BILLABLE_EXPENSE)) {
			vendorsAndPurchasesPage
					.addPreferenceOption(expensesByCustomerOption);
		}
		return vendorsAndPurchasesPage;
	}

	private PreferencePage getCatogiriesInfoPage() {
		PreferencePage catogiriesInfoPage = new PreferencePage(
				messages.categories());
		LocationTrackingOption locationTrackingOption = new LocationTrackingOption();
		ClassTrackingOption classTrackingPage = new ClassTrackingOption();
		JobTrackingOption jobTrackingOption = new JobTrackingOption();
		if (hasPermission(Features.LOCATION)) {
			catogiriesInfoPage.addPreferenceOption(locationTrackingOption);
		}
		if (hasPermission(Features.CLASS)) {
			catogiriesInfoPage.addPreferenceOption(classTrackingPage);
		}
		if (hasPermission(Features.JOB_COSTING)) {
			catogiriesInfoPage.addPreferenceOption(jobTrackingOption);
		}
		return catogiriesInfoPage;
	}

	public boolean hasPermission(String feature) {
		return Accounter.hasPermission(feature);
	}

	private PreferencePage getCompanyInfoPage() {
		PreferencePage companyInfoPage = new PreferencePage(messages.company());

		CompanyInfoOption companyInfoOption = new CompanyInfoOption();
		CompanyDateFormateOption formateOption = new CompanyDateFormateOption();
		CompanyEinOption einOption = new CompanyEinOption();
		// CompanyFiscalYearOption fiscalYearOption = new
		// CompanyFiscalYearOption();
		DoyouUseOption doyouUseOption = new DoyouUseOption();
		AccountNumberRangeOption accountNumberRangeOption = new AccountNumberRangeOption();
		CompanyCurrencyOption currencyOption = new CompanyCurrencyOption();
		CreditsOption creditsOption = new CreditsOption();
		CompanyTimeZoneOption timeZoneOption = new CompanyTimeZoneOption();
		TerminologyOption terminologyOption = new TerminologyOption();
		TrackOrChargeTaxOption taxOption = new TrackOrChargeTaxOption();
		CurrencyFormatOption currencyFormatOption = new CurrencyFormatOption();

		companyInfoPage.addPreferenceOption(companyInfoOption);
		companyInfoPage.addPreferenceOption(formateOption);
		companyInfoPage.addPreferenceOption(einOption);
		// companyInfoPage.addPreferenceOption(fiscalYearOption);
		companyInfoPage.addPreferenceOption(doyouUseOption);
		companyInfoPage.addPreferenceOption(accountNumberRangeOption);
		companyInfoPage.addPreferenceOption(currencyOption);
		companyInfoPage.addPreferenceOption(creditsOption);
		companyInfoPage.addPreferenceOption(timeZoneOption);
		companyInfoPage.addPreferenceOption(terminologyOption);
		companyInfoPage.addPreferenceOption(taxOption);
		companyInfoPage.addPreferenceOption(currencyFormatOption);

		return companyInfoPage;
	}

	private StyledPanel createPageView(final PreferencePage page) {
		final StyledPanel pageView = new StyledPanel("pageView");
		// pageView.setWidth("100%");
		final List<AbstractPreferenceOption> options = page.getOptions();
		final List<Anchor> optionLinks = new ArrayList<Anchor>();
		for (int index = 0; index < options.size(); index++) {
			final AbstractPreferenceOption option = options.get(index);
			final Anchor optionLink = new Anchor(option.getTitle());
			optionLinks.add(optionLink);
			pageView.add(optionLinks.get(index));
			pageView.addStyleName("options_panel");
			optionLinks.get(index).getElement().getParentElement()
					.addClassName("preferences_option");

			optionLinks.get(index).addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					optionLink.addStyleName("optionFocused");
				}
			});
			optionLinks.get(index).addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					optionLink.removeStyleName("optionFocused");
				}
			});
			optionLinks.get(index).addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (int i = 0; i < options.size(); i++) {
						optionLinks.get(i).removeStyleName("optionClicked");
					}
					optionLink.addStyleName("optionClicked");
					pageDetailsPanel.ensureVisible(option);
				}
			});
		}
		return pageView;
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.cancelButton = new CancelButton(this);
		saveAndCloseButton.setText(messages.update());
		cancelButton.setText(messages.close());
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(cancelButton);
		saveAndCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (preferencePages == null) {
					return;
				}
				for (PreferencePage page : preferencePages) {
					page.onSave();
					if (!page.canSave) {
						return;
					}
				}
				Accounter.updateCompany(PreferenceSettingsView.this,
						Accounter.getCompany());
			}
		});
	}

	@Override
	protected void changeButtonBarMode(boolean disable) {

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
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
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		Accounter.reset();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String errorString = AccounterExceptions.getErrorString(exception
				.getErrorCode());
		Accounter.showError(errorString);
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
