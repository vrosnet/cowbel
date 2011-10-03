package com.cowlang2.parser.nodes;

import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;
import com.cowlang2.parser.errors.ExpectedAtom;
import com.cowlang2.parser.tokens.SyntacticElementToken;

public class AtomParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		for (char keyword : S.SingleCharOperators)
		{
			if (location.codepointAtOffset(0) == keyword)
			{
				MutableLocation end = new MutableLocation(location);
				end.advance();
				return new SyntacticElementToken(location, end);
			}
		}
		
		for (String keyword : S.Keywords)
		{
			if (location.matches(keyword))
			{
				if (!Character.isJavaIdentifierPart(keyword.length()))
				{
					MutableLocation end = new MutableLocation(location);
					end.advance(keyword.length());
					return new SyntacticElementToken(location, end);
				}
			}
		}
		
		return new ExpectedAtom(location);
	}
}
