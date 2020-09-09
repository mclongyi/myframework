package com.odianyun.search.whale.api.model.req;

import java.io.Serializable;

import com.odianyun.search.whale.api.model.SuggestType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuggestRequest extends AbstractSearchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SuggestRequest(Integer companyId, String input) {
		super(companyId);
		this.input = input;
	}
	//用户输入
	private String input;
	
	private SuggestType type;

	private Long merchantId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SuggestRequest that = (SuggestRequest) o;

		if (input != null ? !input.equals(that.input) : that.input != null) return false;
		if (type != that.type) return false;
		return !(merchantId != null ? !merchantId.equals(that.merchantId) : that.merchantId != null);

	}

	@Override
	public int hashCode() {
		int result = input != null ? input.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (merchantId != null ? merchantId.hashCode() : 0);
		return result;
	}
}
