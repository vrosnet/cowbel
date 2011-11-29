package com.cowlark.sake.ast.nodes;

import java.util.List;
import com.cowlark.sake.parser.core.Location;

public class StatementListNode extends StatementNode
{
	public StatementListNode(Location start, Location end,
			List<StatementNode> statements)
    {
        super(start, end);
        addChildren(statements);
    }
	
	public StatementListNode(Location start, Location end,
			StatementNode... statements)
	{
		super(start, end);
		addChildren(statements);
	}
}