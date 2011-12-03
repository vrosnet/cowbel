package com.cowlark.sake.ast.nodes;

import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.types.StringType;
import com.cowlark.sake.types.Type;

public class StringTypeNode extends TypeNode
{
	private static Type _type = new StringType();
	
	public StringTypeNode(Location start, Location end)
    {
        super(start, end);
    }
	
	@Override
	public Type constructTypeObject()
	{
	    return _type;
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
}