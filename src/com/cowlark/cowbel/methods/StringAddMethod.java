/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

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