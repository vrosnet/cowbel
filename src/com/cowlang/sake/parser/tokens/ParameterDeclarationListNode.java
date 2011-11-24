package com.cowlang.sake.parser.tokens;

import java.util.List;
import com.cowlang.sake.parser.core.Location;

public class ParameterDeclarationListNode extends ExpressionNode
{
	public ParameterDeclarationListNode(Location start, Location end)
    {
		super(start, end);
    }
	
	public ParameterDeclarationListNode(Location start, Location end,
			List<ParameterDeclarationNode> params)
    {
		this(start, end);
		addChildren(params);
    }
	
	@Override
	public String getShortDescription()
	{
	    return getText();
	}
}