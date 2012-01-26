package com.cowlark.cowbel.methods;

import com.cowlark.cowbel.types.StringType;

public class StringAddMethod extends Method
{
	public StringAddMethod()
    {
		setSignature("string.+.1", "string._add");
		setOutputTypes(StringType.create());
		setInputTypes(StringType.create());
    }
}
