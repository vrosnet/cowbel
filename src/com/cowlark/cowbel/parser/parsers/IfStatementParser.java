/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.AbstractScopeConstructorNode;
import com.cowlark.cowbel.ast.IfElseStatementNode;
import com.cowlark.cowbel.ast.IfStatementNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class IfStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = IfTokenParser.parse(location);
		if (pr.failed())
			return pr;
		
		ParseResult conditionalpr = ExpressionHighParser.parse(pr.end());
		if (conditionalpr.failed())
			return conditionalpr;
		
		ParseResult positivepr = StatementScopeConstructorParser.parse(conditionalpr.end());
		if (positivepr.failed())
			return positivepr;
		
		pr = ElseTokenParser.parse(positivepr.end());
		if (pr.failed())
			return new IfStatementNode(location, positivepr.end(),
					(AbstractExpressionNode) conditionalpr,
					(AbstractScopeConstructorNode) positivepr);
		
		ParseResult negativepr = StatementScopeConstructorParser.parse(pr.end());
		if (negativepr.failed())
			return negativepr;
		
		return new IfElseStatementNode(location, negativepr.end(),
				(AbstractExpressionNode) conditionalpr,
				(AbstractScopeConstructorNode) positivepr,
				(AbstractScopeConstructorNode) negativepr);
	}
}
