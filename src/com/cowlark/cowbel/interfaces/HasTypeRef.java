/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.interfaces;

import com.cowlark.cowbel.core.TypeRef;

public interface HasTypeRef
{
	public TypeRef getTypeRef();
	public void setTypeRef(TypeRef tr);
	public void aliasTypeRef(TypeRef tr);
}
