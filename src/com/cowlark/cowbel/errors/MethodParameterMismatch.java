/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.errors;

import java.util.List;
import com.cowlark.cowbel.ast.Node;
import com.cowlark.cowbel.methods.Method;
import com.cowlark.cowbel.types.Type;

public class MethodParameterMismatch extends CompilationException
{
    private static final long serialVersionUID = 8952338567558812761L;
    
	private Node _node;
    private Method _method;
	private List<Type> _methodoutput;
	private List<Type> _calledoutput;
	private List<Type> _methodinput;
	private List<Type> _calledinput;
    
	public MethodParameterMismatch(Node node, Method method,
			List<Type> methodoutput, List<Type> calledoutput,
			List<Type> methodinput, List<Type> calledinput)
    {
		_node = node;
		_method = method;
		_methodoutput = methodoutput;
		_calledoutput = calledoutput;
		_methodinput = methodinput;
		_calledinput = calledinput;
    }
	
	@Override
	public String getMessage()
	{
		return "Method parameter mismatch in call to " +
				_method.getName() + " at " + _node.locationAsString();
	}
}
