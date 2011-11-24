package com.cowlang.sake.parser.nodes;

import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.MutableLocation;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.errors.ExpectedStringConstant;
import com.cowlang.sake.parser.errors.InvalidCharacterInStringConstant;
import com.cowlang.sake.parser.tokens.StringConstantNode;

public class StringConstantParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		int delimiter = location.codepointAtOffset(0);
		if ((delimiter != '"') && (delimiter != '\''))
			return new ExpectedStringConstant(location);
		
		StringBuilder sb = new StringBuilder();
		MutableLocation end = new MutableLocation(location);
		end.advance();
		
		for (;;)
		{
			int c = end.codepointAtOffset(0);
			end.advance();
			
			if (c == delimiter)
				break;
			
			switch (c)
			{
				case '\\':
					c = end.codepointAtOffset(0);
					end.advance();

					sb.appendCodePoint(c);
					break;

				case -1:
				case '\n':
				case '\r':
					return new InvalidCharacterInStringConstant(end);
					
				default:
					sb.appendCodePoint(c);
			}
		}
		
		return new StringConstantNode(location, end, sb.toString());
	}
}
