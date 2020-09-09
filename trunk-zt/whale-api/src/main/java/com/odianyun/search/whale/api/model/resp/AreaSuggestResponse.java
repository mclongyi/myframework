package com.odianyun.search.whale.api.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AreaSuggestResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<AreaResult> areaResult = new ArrayList<>();

	private long cost;

	private long mapCost;
}
