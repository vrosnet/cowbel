/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import com.cowlark.cowbel.ast.Visitor;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.parser.core.Location;

public class DummyExpressionNode extends ExpressionNode
{
	public DummyExpressionNode(Location start, Location end, ExpressionNode child)
    {
        super(start, end);
        addChild(child);
    }
	
	public ExpressionNode getChild()
	{
		return (ExpressionNode) getChild(0);
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}