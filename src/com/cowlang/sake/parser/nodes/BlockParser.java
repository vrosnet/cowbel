package com.cowlang.sake.parser.nodes;

import java.util.ArrayList;
import com.cowlang.sake.parser.core.Location;
import com.cowlang.sake.parser.core.ParseResult;
import com.cowlang.sake.parser.tokens.BlockNode;
import com.cowlang.sake.parser.tokens.StatementNode;

public class BlockParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr = OpenBraceParser.parse(location);
		if (pr.failed())
			return pr;
		
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		Location n = pr.end();
		for (;;)
		{
			pr = CloseBraceParser.parse(n);
			if (pr.success())
			{
				n = pr.end();
				break;
			}
			
			pr = FunctionStatementParser.parse(n);
			if (pr.failed())
				return pr;
			
			statements.add((StatementNode) pr);
			n = pr.end();
		}
		
		return new BlockNode(location, n, statements); 
	}
}