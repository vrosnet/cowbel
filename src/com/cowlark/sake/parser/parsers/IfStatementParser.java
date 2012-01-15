package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.IfElseStatementNode;
import com.cowlark.sake.ast.nodes.IfStatementNode;
import com.cowlark.sake.ast.nodes.ScopeConstructorNode;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
		
		ParseResult positivepr = ScopeConstructorParser.parse(conditionalpr.end());
		if (positivepr.failed())
			return positivepr;
		
		pr = ElseTokenParser.parse(positivepr.end());
		if (pr.failed())
			return new IfStatementNode(location, positivepr.end(),
					(ExpressionNode) conditionalpr,
					(ScopeConstructorNode) positivepr);
		
		ParseResult negativepr = ScopeConstructorParser.parse(pr.end());
		if (negativepr.failed())
			return negativepr;
		
		return new IfElseStatementNode(location, negativepr.end(),
				(ExpressionNode) conditionalpr,
				(ScopeConstructorNode) positivepr,
				(ScopeConstructorNode) negativepr);
	}
}
