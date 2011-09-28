package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.ParseResult;

public class StatementsParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		return StatementParser.parse(location); 
	}
}
