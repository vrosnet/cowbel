package com.cowlark.sake;

import com.cowlark.sake.ast.nodes.IdentifierNode;
import com.cowlark.sake.ast.nodes.LabelStatementNode;

public class Label
{
	private LabelStatementNode _node;
	
	public Label(LabelStatementNode node)
    {
		_node = node;
    }
	
	public IdentifierNode getLabelName()
	{
		return _node.getLabelName();
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + getLabelName().getText();
	}
}