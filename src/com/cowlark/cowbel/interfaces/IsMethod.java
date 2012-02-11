/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.interfaces;

import com.cowlark.cowbel.ast.AbstractExpressionNode;

public interface IsMethod extends HasIdentifier
{
	public AbstractExpressionNode getReceiver();
}
