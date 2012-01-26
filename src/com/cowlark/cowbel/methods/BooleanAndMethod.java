package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.BooleanType;

public class BooleanAndMethod extends Method
{
	public BooleanAndMethod()
    {
		setSignature("boolean.&.1", "boolean._and");
		setOutputTypes(BooleanType.create());
		setInputTypes(BooleanType.create());
    }
}
