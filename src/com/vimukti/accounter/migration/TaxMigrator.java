package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXItemGroup;

public class TaxMigrator<T extends TAXItemGroup> implements IMigrator<T> {

	@Override
	public JSONObject migrate(T obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("name", obj.getName());
		jsonObject.put("isInactive", !obj.isActive());
		jsonObject.put("isTaxGroup", false);
		return jsonObject;
	}
}