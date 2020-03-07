package main.filters;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FilterParentTest {
	private static FilterParent filter;
	String[] includes = new String[] {"/include","/authentication/index.html","/register/mamad","/register/*ali"};
	String[] nonIncludes = new String[] {"/includ","/iclude","/authentication/indexhtml","/regiter/mamad","/registeer/*ali"};
	String[] excludes = new String[] {"/exclude","/login/mamad","/login/ali","/login/ahmad"};
	String[] nonExcludes = new String[] {"/exlude","/excludee","/logn/mamad","/logiin/ali","/logqin/ahmad"};
	
	@BeforeAll
	public static void setup() {
		filter = new FilterParent() {
			
			@Override
			protected String[] includationPathExpression() {
				return new String[] {"/include","^/authentication/index.html$","^/register/.*"};
			}
			
			@Override
			protected String[] excludationPathExpression() {
				return new String[] {"/exclude","/login/mamad","/login/.*"};
			}
		};
	}
	
	@Test
	public void testIsInIncludation() throws Exception {
		for (String include : includes) {
			if(!filter.isInIncludation(include)) {
				throw new Exception("an includable path doesn't allow in isInIncludation method => "+include);
			}
		}
		
		for(String nonInclude : nonIncludes) {
			if(filter.isInIncludation(nonInclude)) {
				throw new Exception("a non-includable path does allow in isInIncludation method => "+nonInclude);
			}
		}
	}
	
	@Test
	public void testIsInExcludation() throws Exception {
		for (String exclude : excludes) {
			if(!filter.isInExcludation(exclude)) {
				throw new Exception("an excludable path doesn't allow in isInExcludation method => "+exclude);
			}
		}
		
		for(String nonExclude : nonExcludes) {
			if(filter.isInExcludation(nonExclude)) {
				throw new Exception("a non-excludable path does allow in isInExcludation method => "+nonExclude);
			}
		}
	}
}
