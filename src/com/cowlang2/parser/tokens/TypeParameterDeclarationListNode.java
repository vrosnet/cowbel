package com.cowlang2.parser.tokens;

import java.util.List;
import com.cowlang2.parser.core.Location;

public class TypeParameterDeclarationListNode extends ExpressionNode
{
	public TypeParameterDeclarationListNode(Location start, Location end,
			List<IdentifierNode> identifiers)
    {
		super(start, end);
		addChildren(identifiers);
    }
	
	@Override
	public String getShortDescription()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		
		boolean first = true;
		for (IdentifierNode t : (Iterable<? extends IdentifierNode>)getChildren())
		{
			if (!first)
				sb.append(" ");
			else
				first = false;
			
			sb.append(t.getText());
		}
		
		sb.append(">");
		return sb.toString();
	}
}