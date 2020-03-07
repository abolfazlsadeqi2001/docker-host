package main.filters;

import java.util.regex.Pattern;

public abstract class FilterParent {
	
	/** 
	 * one of include and exclude must involve whole pathes (another one must check before if it doesn't then use this one(include whole paths))
	 * @return return an array of all expression for exclude on affect filter on it
	 */
	protected abstract String[] excludationPathExpression();
	/** 
	 * one of include and exclude must involve whole pathes (another one must check before if it doesn't then use this one(include whole paths))
	 * @return return an array of all expression for include on affect filter on it
	 */
	protected abstract String[] includationPathExpression();
	
	/**
	 * @param path path to affect filter like (/register)
	 * @return return if in excludations
	 */
	protected boolean isInExcludation(String path) {
		for (String currentExcludationRegex : excludationPathExpression()) {
			if(Pattern.matches(currentExcludationRegex, path)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param path path to affect filter like (/register)
	 * @return return if in includations
	 */
	protected boolean isInIncludation(String path) {
		for (String currentIncludationRegex : includationPathExpression()) {
			if(Pattern.matches(currentIncludationRegex, path)) {
				return true;
			}
		}
		
		return false;
	}
}
