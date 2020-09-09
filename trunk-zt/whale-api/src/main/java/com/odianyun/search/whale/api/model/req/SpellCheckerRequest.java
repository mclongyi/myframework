package com.odianyun.search.whale.api.model.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 拼写检查请求
 *
 * @author jing.liu
 *
 */
@Data
@NoArgsConstructor
public class SpellCheckerRequest extends AbstractSearchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	public SpellCheckerRequest(Integer companyId) {
		super(companyId);
	}

	//用户输入
	private String input;
}
