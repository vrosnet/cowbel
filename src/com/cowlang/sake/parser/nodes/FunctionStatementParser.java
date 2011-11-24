package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;

public class FunctionStatementParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = VarAssignmentParser.parse(location);
		if (pr1.success())
			return pr1;
		
		ParseResult pr2 = VarDeclParser.parse(location);
		if (pr2.success())
			return pr2;
		
		ParseResult pr3 = ExpressionStatementParser.parse(location);
		if (pr3.success())
			return pr3;
		
		return combineParseErrors(pr1, pr2, pr3);
	}
}