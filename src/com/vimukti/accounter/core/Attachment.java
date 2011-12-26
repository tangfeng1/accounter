package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.main.upload.UploadAttachment;
import com.vimukti.accounter.main.upload.AttachmentFileServer;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Attachment extends CreatableObject implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attachmentId;
	private String name;
	private long size;

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		boolean onSave = super.onSave(session);
		if (getID() == 0) {
			setCompany(getCreatedBy().getCompany());
			UploadAttachment attachment = new UploadAttachment(attachmentId,
					UploadAttachment.CREATE, getName());
			attachment.key = getCompany().getEncryptionKey().getBytes();
			AttachmentFileServer.put(attachment);
		}
		return onSave;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		UploadAttachment attachment = new UploadAttachment(attachmentId,
				UploadAttachment.DELETE, getName());
		AttachmentFileServer.put(attachment);
		return super.onDelete(arg0);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}
}
