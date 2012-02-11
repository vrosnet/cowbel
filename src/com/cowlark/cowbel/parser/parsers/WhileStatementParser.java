/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.WhileStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class WhileStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = WhileTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult conditionalpr = ExpressionLowParser.parse(pr.end());
		if (conditionalpr.failed())
			return conditionalpr;
		
		ParseResult bodypr = StatementScopeConstructorParser.parse(conditionalpr.end());
		if (bodypr.failed())
			return bodypr;
		
		return new WhileStatementNode(location, bodypr.end(),
				(AbstractExpressionNode) conditionalpr,
				(AbstractScopeConstructorNode) bodypr);
	}
}
