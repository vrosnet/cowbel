/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.core;

import com.cowlark.cowbel.errors.CompilationException;

public abstract class BasicBlockVisitor
{
	public abstract void visit(BasicBlock bb) throws CompilationException;
}