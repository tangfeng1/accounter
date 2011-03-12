package com.vimukti.accounter.web.client.core;

public class ClientBrandingTheme implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7185358806684919637L;
	public static final String FONT_ARIAL = "Arial";
	public static final String FONT_CALIBIRI = "Calibiri";
	public static final String FONT_CAMBRIA = "Cambria";
	public static final String FONT_GEORGIA = "Georgia";
	public static final String FONT_MY_RIAD = "Myriad";
	public static final String FONT_TAHOMA = "Tahoma";
	public static final String FONT_TIMES_NEW_ROMAN = "Times New Roman";
	public static final String FONT_TREBUCHET = "Trebuchet";

	public static final int PAGE_SIZE_A4 = 1;
	public static final int PAGE_SIZE_US_LETTER = 2;

	public static final int MARGIN_MEASURES_IN_CM = 1;
	public static final int MARGIN_MEASURES_IN_INCHES = 2;

	public static final int LOGO_ALIGNMENT_LEFT = 1;
	public static final int LOGO_ALIGNMENT_RIGHT = 2;

	public static final int SHOW_TAXES_AS_EXCLUSIVE = 1;
	public static final int SHOW_TAXES_AS_INCLUSIVE = 2;

	private long id;
	private String stringId;
	private String themeName;
	private int pageSizeType;
	private double bottomMargin;
	private double topMargin;
	private int marginsMeasurementType;
	private double addressPadding;
	private String font;
	private String fontSize;
	private String openInvoiceTitle;
	private String overDueInvoiceTitle;
	private String creditMemoTitle;
	private String statementTitle;

	private boolean isShowTaxNumber;
	private boolean isShowColumnHeadings;
	private boolean isShowUnitPrice_And_Quantity;
	private boolean isShowPaymentAdviceCut_Away;
	private boolean isShowTaxColumn;
	private boolean isShowRegisteredAddress;
	private boolean isShowLogo;

	private String payPalEmailID;
	private int logoAlignmentType;
	private int showTaxesAsType;
	private String contactDetails;
	private String Terms_And_Payment_Advice;

	private transient boolean isImported;
	private transient boolean isOnSaveProccessed;

	String createdBy;
	String lastModifier;
	ClientFinanceDate createdDate;
	ClientFinanceDate lastModifiedDate;

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BRANDINGTHEME;
	}

	@Override
	public String getStringID() {
		return stringId;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringId = stringID;

	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setPageSizeType(int pageSizeType) {
		this.pageSizeType = pageSizeType;
	}

	public int getPageSizeType() {
		return pageSizeType;
	}

	public void setTopMargin(double topMargin) {
		this.topMargin = topMargin;
	}

	public double getTopMargin() {
		return topMargin;
	}

	public void setBottomMargin(double bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public double getBottomMargin() {
		return bottomMargin;
	}

	public void setMarginsMeasurementType(int marginsMeasurementType) {
		this.marginsMeasurementType = marginsMeasurementType;
	}

	public int getMarginsMeasurementType() {
		return marginsMeasurementType;
	}

	public void setAddressPadding(double addressPadding) {
		this.addressPadding = addressPadding;
	}

	public double getAddressPadding() {
		return addressPadding;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFont() {
		return font;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setOpenInvoiceTitle(String openInvoiceTitle) {
		this.openInvoiceTitle = openInvoiceTitle;
	}

	public String getOpenInvoiceTitle() {
		return openInvoiceTitle;
	}

	public void setOverDueInvoiceTitle(String overDueInvoiceTitle) {
		this.overDueInvoiceTitle = overDueInvoiceTitle;
	}

	public String getOverDueInvoiceTitle() {
		return overDueInvoiceTitle;
	}

	public void setCreditMemoTitle(String creditMemoTitle) {
		this.creditMemoTitle = creditMemoTitle;
	}

	public String getCreditMemoTitle() {
		return creditMemoTitle;
	}

	public void setStatementTitle(String statementTitle) {
		this.statementTitle = statementTitle;
	}

	public String getStatementTitle() {
		return statementTitle;
	}

	public void setShowTaxNumber(boolean isShowTaxNumber) {
		this.isShowTaxNumber = isShowTaxNumber;
	}

	public boolean isShowTaxNumber() {
		return isShowTaxNumber;
	}

	public void setShowColumnHeadings(boolean isShowColumnHeadings) {
		this.isShowColumnHeadings = isShowColumnHeadings;
	}

	public boolean isShowColumnHeadings() {
		return isShowColumnHeadings;
	}

	public void setShowUnitPrice_And_Quantity(
			boolean isShowUnitPrice_And_Quantity) {
		this.isShowUnitPrice_And_Quantity = isShowUnitPrice_And_Quantity;
	}

	public boolean isShowUnitPrice_And_Quantity() {
		return isShowUnitPrice_And_Quantity;
	}

	public void setShowPaymentAdviceCut_Away(boolean isShowPaymentAdviceCut_Away) {
		this.isShowPaymentAdviceCut_Away = isShowPaymentAdviceCut_Away;
	}

	public boolean isShowPaymentAdviceCut_Away() {
		return isShowPaymentAdviceCut_Away;
	}

	public void setShowTaxColumn(boolean isShowTaxColumn) {
		this.isShowTaxColumn = isShowTaxColumn;
	}

	public boolean isShowTaxColumn() {
		return isShowTaxColumn;
	}

	public void setShowRegisteredAddress(boolean isShowRegisteredAddress) {
		this.isShowRegisteredAddress = isShowRegisteredAddress;
	}

	public boolean isShowRegisteredAddress() {
		return isShowRegisteredAddress;
	}

	public void setShowLogo(boolean isShowLogo) {
		this.isShowLogo = isShowLogo;
	}

	public boolean isShowLogo() {
		return isShowLogo;
	}

	public void setPayPalEmailID(String payPalEmailID) {
		this.payPalEmailID = payPalEmailID;
	}

	public String getPayPalEmailID() {
		return payPalEmailID;
	}

	public void setLogoAlignmentType(int logoAlignmentType) {
		this.logoAlignmentType = logoAlignmentType;
	}

	public int getLogoAlignmentType() {
		return logoAlignmentType;
	}

	public void setShowTaxesAsType(int showTaxesAsType) {
		this.showTaxesAsType = showTaxesAsType;
	}

	public int getShowTaxesAsType() {
		return showTaxesAsType;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setTerms_And_Payment_Advice(String terms_And_Payment_Advice) {
		Terms_And_Payment_Advice = terms_And_Payment_Advice;
	}

	public String getTerms_And_Payment_Advice() {
		return Terms_And_Payment_Advice;
	}

	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	public boolean isImported() {
		return isImported;
	}

	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

}
