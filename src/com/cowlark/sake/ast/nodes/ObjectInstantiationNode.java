package com.cowlark.sake.ast.nodes;

import java.util.List;
import com.cowlark.sake.parser.core.Location;

public class ObjectInstantiationNode extends ExpressionNode
{
	public ObjectInstantiationNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ObjectInstantiationNode(Location start, Location end,
			List<ObjectInstantiationMemberNode> members)
	{
		this(start, end);
		addChildren(members);
	}
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}