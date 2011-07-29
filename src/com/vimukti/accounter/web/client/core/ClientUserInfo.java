/**
 * 
 */
package com.vimukti.accounter.web.client.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientUserInfo implements IAccounterCore {

	private long id;

	private String firstName;

	private String lastName;

	private String fullName;

	private String email;

	private String userRole;

	private ClientUserPermissions permissions;

	private boolean canDoUserManagement;

	private String displayName;

	private boolean isAdmin;

	private int loginCount;

	private long lastLogin;

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole
	 *            the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return the permissions
	 */
	public ClientUserPermissions getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(ClientUserPermissions permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the canDoUserManagement
	 */
	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	/**
	 * @param canDoUserManagement
	 *            the canDoUserManagement to set
	 */
	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin
	 *            the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the loginCount
	 */
	public int getLoginCount() {
		return loginCount;
	}

	/**
	 * @param loginCount
	 *            the loginCount to set
	 */
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	/**
	 * @return the lastLogin
	 */
	public long getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Override
	public String getName() {
		if (getFirstName() == null && getLastName() == null)
			return "";
		else if (getFirstName() == null)
			return getLastName();
		else if (getLastName() == null)
			return getFirstName();
		return getFirstName() + " " + getLastName();

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.USER;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientUserInfo";
	}

}
