package com.odianyun.search.whale.api.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 拼写检查应答
 *
 * @author jing liu
 *
 */
@Data
@NoArgsConstructor
public class SpellCheckerResponse extends BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	//原始文本
	private String text;
	//分词后列表
	private List<String> tokens;
	//检查结果
	private Map<String, List<String>> results;
}
