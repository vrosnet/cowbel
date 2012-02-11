/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import com.cowlark.cowbel.ast.AbstractExpressionNode;
import com.cowlark.cowbel.ast.ExpressionListNode;
import com.cowlark.cowbel.ast.IdentifierNode;
import com.cowlark.cowbel.ast.MethodCallExpressionNode;
import com.cowlark.cowbel.ast.TypeListNode;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.ParseResult;

public class PrefixOperatorParser extends Parser
{
	@Override
	protected ParseResult parseImpl(Location location)
	{
		ParseResult pr1 = OperatorParser.parse(location);
		if (pr1.failed())
			return pr1;
		
		ParseResult pr2 = ExpressionMediumParser.parse(pr1.end());
		if (pr2.failed())
			return pr2;
		
		return new MethodCallExpressionNode(location, pr2.end(),
				(AbstractExpressionNode)pr2, (IdentifierNode)pr1,
				new TypeListNode(pr1.start(), pr1.end()),
				new ExpressionListNode(pr1.start(), pr1.end()));
	}
}
