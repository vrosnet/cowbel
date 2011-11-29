package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class FunctionDefinitionNode extends StatementNode
{
	public FunctionDefinitionNode(Location start, Location end,
			FunctionHeaderNode header, BlockNode body)
    {
		super(start, end);
		addChild(header);
		addChild(body);
    }	
}