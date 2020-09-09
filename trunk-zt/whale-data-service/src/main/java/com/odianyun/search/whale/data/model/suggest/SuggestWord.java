package com.odianyun.search.whale.data.model.suggest;

import com.odianyun.search.whale.api.model.SuggestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * based suggest to use for search drop-down box
 * @author yuqian
 *
 */
public class SuggestWord {

	private String keyword;
	private String spell;
	private String firstSpell;
	private String characterSort;
	private String characterCut;
	private Integer searchResults;
	private Integer searchFrequency;
	private Integer companyId;
	private List<Long> merchantIdList;
	private Integer isArea = SuggestType.KEYWORD.getCode();

	private String input;
	
	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("keyword","keyword" );
    	resultMap.put("spell","spell" );
    	resultMap.put("firstSpell","first_spell" );
    	resultMap.put("characterCut","character_cut" );
    	resultMap.put("characterSort","character_sort" );
    	resultMap.put("searchFrequency","search_frequency" );
    	resultMap.put("searchResults","search_results" );
    	resultMap.put("companyId","company_id" );
    	resultMap.put("input","input" );
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getFirstSpell() {
		return firstSpell;
	}
	public void setFirstSpell(String firstSpell) {
		this.firstSpell = firstSpell;
	}
	public String getCharacterSort() {
		return characterSort;
	}
	public void setCharacterSort(String characterSort) {
		this.characterSort = characterSort;
	}
	public String getCharacterCut() {
		return characterCut;
	}
	public void setCharacterCut(String characterCut) {
		this.characterCut = characterCut;
	}
	public Integer getSearchResults() {
		return searchResults;
	}
	public void setSearchResults(Integer searchResults) {
		this.searchResults = searchResults;
	}
	public Integer getSearchFrequency() {
		return searchFrequency;
	}
	public void setSearchFrequency(Integer searchFrequency) {
		this.searchFrequency = searchFrequency;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public List<Long> getMerchantIdList() {
		return merchantIdList;
	}

	public void setMerchantIdList(List<Long> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}

	public Integer getIsArea() {
		return isArea;
	}

	public void setIsArea(Integer isArea) {
		this.isArea = isArea;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SuggestWord that = (SuggestWord) o;

		if (keyword != null ? !keyword.equals(that.keyword) : that.keyword != null) return false;
		if (spell != null ? !spell.equals(that.spell) : that.spell != null) return false;
		if (firstSpell != null ? !firstSpell.equals(that.firstSpell) : that.firstSpell != null) return false;
		if (characterSort != null ? !characterSort.equals(that.characterSort) : that.characterSort != null)
			return false;
		if (characterCut != null ? !characterCut.equals(that.characterCut) : that.characterCut != null) return false;
		if (searchResults != null ? !searchResults.equals(that.searchResults) : that.searchResults != null)
			return false;
		if (searchFrequency != null ? !searchFrequency.equals(that.searchFrequency) : that.searchFrequency != null)
			return false;
		if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
		return !(input != null ? !input.equals(that.input) : that.input != null);

	}

	@Override
	public int hashCode() {
		int result = keyword != null ? keyword.hashCode() : 0;
		result = 31 * result + (spell != null ? spell.hashCode() : 0);
		result = 31 * result + (firstSpell != null ? firstSpell.hashCode() : 0);
		result = 31 * result + (characterSort != null ? characterSort.hashCode() : 0);
		result = 31 * result + (characterCut != null ? characterCut.hashCode() : 0);
		result = 31 * result + (searchResults != null ? searchResults.hashCode() : 0);
		result = 31 * result + (searchFrequency != null ? searchFrequency.hashCode() : 0);
		result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
		result = 31 * result + (input != null ? input.hashCode() : 0);
		return result;
	}
}
