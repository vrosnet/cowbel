/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel;

public interface CompilerListener
{
	public void onParseBegin();
	public void onParseEnd();
	
	public void onSymbolTableAnalysisBegin();
	public void onSymbolTableAnalysisEnd();
	
	public void onTypeCheckBegin();
	public void onTypeCheckEnd();
	
	public void onBasicBlockAnalysisBegin();
	public void onBasicBlockAnalysisEnd();
	
	public void onDataflowAnalysisBegin();
	public void onDataflowAnalysisEnd();
	
	public void onCodeGenerationBegin();
	public void onCodeGenerationEnd();
}