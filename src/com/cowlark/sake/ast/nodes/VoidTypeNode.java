package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.parser.core.Location;

public class VoidTypeNode extends TypeNode
{
	public VoidTypeNode(Location start, Location end)
    {
        super(start, end);
    }
}