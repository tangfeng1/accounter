package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewSalesPersonCommand extends NewAbstractCommand {

	private static final String SALES_PERSON_NAME = "salesPersonName";
	private static final String FILE_AS = "fileAs";
	private static final String JOB_TITLE = "jobTitle";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String E_MAIL = "eMail";
	private static final String WEB_PAGE_ADDRESS = "webPageAddress";
	private static final String GENDER = "gender";
	private static final String DO_BIRTH = "dateOfBirth";
	private static final String DO_HIRE = "dateOfHire";
	private static final String DO_LASTREVIEW = "dateOfLastReview";
	private static final String DO_RELEASE = "dateOfRelease";
	private static final String MEMO = "memo";
	private static final String ACTIVE = "active";
	ClientSalesPerson salesPerson;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(SALES_PERSON_NAME, getMessages()
				.pleaseEnter(getConstants().salesPersonName()), getConstants()
				.salesPersonName(), false, true) {

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				get(FILE_AS).setValue(value);
			}
		});

		list.add(new NameRequirement(FILE_AS, getMessages().pleaseEnter(
				getConstants().fileAs() + " " + getConstants().name()),
				getConstants().fileAs(), true, true));

		list.add(new NameRequirement(JOB_TITLE, getMessages().pleaseEnter(
				getConstants().jobTitle()), getConstants().jobTitle(), true,
				true));

		list.add(new AddressRequirement(ADDRESS, getMessages().pleaseEnter(
				getConstants().address()), getConstants().address(), true, true));
		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));
		list.add(new NumberRequirement(FAX, getMessages().pleaseEnter(
				getConstants().fax()), getConstants().fax(), true, true));

		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages()
				.pleaseSelect(
						getConstants().expense() + " "
								+ getConstants().account()), getConstants()
				.expense() + " " + getConstants().account(), true, false,
				new ChangeListner<Account>() {

					@Override
					public void onSelection(Account value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_EXPENSE
									|| e.getType() == Account.TYPE_OTHER_ASSET) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return "No bank acounts available";
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});
		list.add(new EmailRequirement(E_MAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), true, true));
		list.add(new NameRequirement(WEB_PAGE_ADDRESS, getMessages()
				.pleaseEnter(getConstants().webPageAddress()), getConstants()
				.webPageAddress(), true, true));
		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().active();
			}

			@Override
			protected String getFalseString() {
				return getConstants().inActive();
			}
		});

		list.add(new StringListRequirement(GENDER, getMessages().pleaseEnter(
				getConstants().gender()), getConstants().gender(), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().gender());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getGenders();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().gender());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new DateRequirement(DO_BIRTH, getMessages().pleaseEnter(
				getConstants().dateofBirth()), getConstants().dateofBirth(),
				true, true));
		list.add(new DateRequirement(DO_HIRE, getMessages().pleaseEnter(
				getConstants().dateofHire()), getConstants().dateofHire(),
				true, true));
		list.add(new DateRequirement(DO_LASTREVIEW, getMessages().pleaseEnter(
				getConstants().dateofLastReview()), getConstants()
				.dateofLastReview(), true, true));
		list.add(new DateRequirement(DO_RELEASE, getMessages().pleaseEnter(
				getConstants().dateofRelease()),
				getConstants().dateofRelease(), true, true));
		list.add(new NameRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(SALES_PERSON_NAME).getValue();
		salesPerson.setFirstName(name);

		String fileAs = get(FILE_AS).getValue();
		salesPerson.setFileAs(fileAs);

		String jobTitle = get(JOB_TITLE).getValue();
		salesPerson.setJobTitle(jobTitle);

		ClientAddress address = get(ADDRESS).getValue();
		salesPerson.setAddress(address);

		String phone = get(PHONE).getValue();
		salesPerson.setPhoneNo(phone);

		boolean isactive = (Boolean) get(ACTIVE).getValue();
		salesPerson.setActive(isactive);

		String fax = get(FAX).getValue();
		salesPerson.setFaxNo(fax);

		Account value = get(EXPENSE_ACCOUNT).getValue();
		salesPerson.setExpenseAccount(value != null ? value.getID() : 0);

		String email = get(E_MAIL).getValue();
		salesPerson.setEmail(email);

		String webPage = get(WEB_PAGE_ADDRESS).getValue();
		salesPerson.setWebPageAddress(webPage);

		String gender = get(GENDER).getValue();
		salesPerson.setGender(gender);

		ClientFinanceDate do_birth = get(DO_BIRTH).getValue();
		salesPerson.setDateOfBirth(do_birth);

		ClientFinanceDate do_hire = get(DO_HIRE).getValue();
		salesPerson.setDateOfHire(do_hire);

		if (do_birth.getDate() != 0) {
			long mustdate = new ClientFinanceDate().getDate() - 180000;
			if ((new ClientFinanceDate(mustdate).before(do_birth))) {
				Result result = new Result();
				result.add(getConstants()
						.dateofBirthshouldshowmorethan18years()
						+ ". Because Sales Person should have 18 years");
				return result;
			}
		}

		ClientFinanceDate do_lastreview = get(DO_LASTREVIEW).getValue();
		salesPerson.setDateOfLastReview(do_lastreview);

		ClientFinanceDate do_release = get(DO_RELEASE).getValue();
		salesPerson.setDateOfRelease(do_release);

		String memo = get(MEMO).getValue();
		salesPerson.setMemo(memo);
		create(salesPerson, context);
		return null;

	}

	private List<String> getGenders() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getConstants().unspecified());
		list.add(getConstants().male());
		list.add(getConstants().female());
		return list;
	}

	@Override
	protected String getDetailsMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages().readyToCreate(getConstants().salesPerson());
		} else {
			return getMessages().readyToUpdate(getConstants().salesPerson());
		}
	}

	@Override
	public String getSuccessMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages().createSuccessfully(
					getConstants().salesPerson());
		} else {
			return getMessages().updateSuccessfully(
					getConstants().salesPerson());
		}
	}

	@Override
	protected String getWelcomeMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages().creating(getConstants().salesPerson());
		} else {
			return "Updateing" + getConstants().salesPerson() + " "
					+ salesPerson.getDisplayName();
		}
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				return "Sales Person List";
			}
			ClientSalesPerson salesPersonByName = CommandUtils
					.getSalesPersonByName(context.getCompany(), string);
			if (salesPersonByName == null) {
				salesPersonByName = CommandUtils.getSalesPersonByNumber(
						context.getCompany(), getNumberFromString(string));
				if (salesPersonByName == null) {
					return "Sales Person List " + string;
				}
			}
			salesPerson = salesPersonByName;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(SALES_PERSON_NAME).setValue(string);
			}
			salesPerson = new ClientSalesPerson();
		}
		return null;
	}

	private void setValues() {
		get(SALES_PERSON_NAME).setValue(salesPerson.getName());
		get(FILE_AS).setValue(salesPerson.getFileAs());
		get(JOB_TITLE).setValue(salesPerson.getJobTitle());
		get(ADDRESS).setValue(salesPerson.getAddress());
		get(PHONE).setValue(salesPerson.getPhoneNo());
		get(ACTIVE).setValue(salesPerson.isActive());
		get(FAX).setValue(salesPerson.getFaxNo());
		get(EXPENSE_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						salesPerson.getExpenseAccount(),
						AccounterCoreType.ACCOUNT));
		get(E_MAIL).setValue(salesPerson.getEmail());
		get(WEB_PAGE_ADDRESS).setValue(salesPerson.getWebPageAddress());
		get(GENDER).setValue(salesPerson.getGender());
		get(DO_BIRTH).setValue(
				new ClientFinanceDate(salesPerson.getDateOfBirth()));
		get(DO_HIRE).setValue(
				new ClientFinanceDate(salesPerson.getDateOfHire()));
		get(DO_LASTREVIEW).setValue(
				new ClientFinanceDate(salesPerson.getDateOfLastReview()));
		get(DO_RELEASE).setValue(
				new ClientFinanceDate(salesPerson.getDateOfRelease()));
		get(MEMO).setValue(salesPerson.getMemo());
	}

	@Override
	protected void setDefaultValues(Context context) {
		String name = get(SALES_PERSON_NAME).getValue();
		if (get(FILE_AS).getValue() == null)
			get(FILE_AS).setDefaultValue(name);
		get(JOB_TITLE).setDefaultValue("");
		get(ADDRESS).setDefaultValue(new ClientAddress());
		get(PHONE).setDefaultValue("");
		get(FAX).setDefaultValue("");
		get(EXPENSE_ACCOUNT).setDefaultValue(null);
		get(E_MAIL).setDefaultValue("");
		get(WEB_PAGE_ADDRESS).setDefaultValue("");
		get(GENDER).setDefaultValue(getConstants().unspecified());
		get(DO_BIRTH).setDefaultValue(new ClientFinanceDate());
		get(DO_HIRE).setDefaultValue(new ClientFinanceDate());
		get(DO_LASTREVIEW).setDefaultValue(new ClientFinanceDate());
		get(DO_RELEASE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);

	}

}
