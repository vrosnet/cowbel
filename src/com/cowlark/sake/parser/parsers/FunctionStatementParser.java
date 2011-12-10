package com.cowlark.sake.parser.parsers;

import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.ParseResult;

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
		
		ParseResult pr3 = ReturnStatementParser.parse(location);
		if (pr3.success())
			return pr3;
		
		ParseResult pr4 = IfStatementParser.parse(location);
		if (pr4.success())
			return pr4;
		
		ParseResult pr5 = ExpressionStatementParser.parse(location);
		if (pr5.success())
			return pr5;
		
		return combineParseErrors(pr1, pr2, pr3, pr4, pr5);
	}
}
