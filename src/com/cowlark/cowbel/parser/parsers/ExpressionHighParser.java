/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.DirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.IndirectFunctionCallExpressionNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class ExpressionHighParser extends Parser
{
	private ParseResult parseMethodCall(ParseResult seed, Location location)
	{
		ParseResult methodpr = MethodNameParser.parse(location);
		if (methodpr.failed())
			return methodpr;
		
		ParseResult typeargspr = TypeListParser.parse(methodpr.end());
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult arguments = ArgumentListParser.parse(typeargspr.end());
		if (arguments.failed())
			return arguments;
		
		return new MethodCallExpressionNode(location, arguments.end(),
				(AbstractExpressionNode) seed, (IdentifierNode) methodpr,
				(TypeListNode) typeargspr,
				(ExpressionListNode) arguments);
	}
	
	private ParseResult parseFunctionCall(ParseResult seed, Location location)
	{
		ParseResult typeargspr = TypeListParser.parse(location);
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult argumentspr = ArgumentListParser.parse(typeargspr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		return new IndirectFunctionCallExpressionNode(location, argumentspr.end(),
				(AbstractExpressionNode)seed, (ExpressionListNode) argumentspr);
	}
	
	private ParseResult parseDirectFunctionCall(Location location)
	{
		ParseResult identifierpr = IdentifierParser.parse(location);
		if (identifierpr.failed())
			return identifierpr;
		
		ParseResult typeargspr = TypeListParser.parse(identifierpr.end());
		if (typeargspr.failed())
			return typeargspr;
		
		ParseResult argumentspr = ArgumentListParser.parse(typeargspr.end());
		if (argumentspr.failed())
			return argumentspr;
		
		return new DirectFunctionCallExpressionNode(location, argumentspr.end(),
				(IdentifierNode) identifierpr,
				(TypeListNode) typeargspr,
				(ExpressionListNode) argumentspr);
	}
	
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult seed = parseDirectFunctionCall(location);
		if (seed.failed())
			seed = ExpressionLeafParser.parse(location);
		if (seed.failed())
			return seed;
		
		for (;;)
		{
			ParseResult pr = DotParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseMethodCall(seed, pr.end());
				if (seed.failed())
					return seed;
				continue;
			}
			
			pr = OpenAngleBracketParser.parse(seed.end());
			if (pr.success())
			{
				ParseResult funcpr = parseFunctionCall(seed, seed.end());
				if (funcpr.failed())
				{
					/* Ambiguity! We will fail here on expressons like a < b.
					 * We want the a to be parsed, so instead of returning an
					 * error, we return the seed. */
					return seed;
				}
				seed = funcpr;
				continue;
			}
			
			pr = OpenParenthesisParser.parse(seed.end());
			if (pr.success())
			{
				seed = parseFunctionCall(seed, pr.end());
				if (seed.failed())
					return seed;
				continue;
			}
			
			break;
		}
		
		return seed;
	}
}
