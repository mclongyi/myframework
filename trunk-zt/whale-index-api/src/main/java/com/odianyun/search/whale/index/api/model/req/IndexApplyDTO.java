package com.odianyun.search.whale.index.api.model.req;

import java.io.Serializable;
import java.util.List;

import com.odianyun.search.whale.index.api.common.UpdateType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IndexApplyDTO implements Serializable{

	private static final long serialVersionUID = 2575616231418053131L;
	private List<Long> ids;
	private UpdateType updateType;
	private int companyId;
	
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public UpdateType getUpdateType() {
		return updateType;
	}
	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	
}
