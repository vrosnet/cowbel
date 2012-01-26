/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.methods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.cowlark.cowbel.ast.nodes.Node;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.MethodParameterMismatch;
import com.cowlark.cowbel.errors.TypesNotCompatibleException;
import com.cowlark.cowbel.types.Type;

public abstract class Method
{
	private static HashMap<String, Method> _primitiveMethods;
	private static HashMap<String, TemplatedMethod.Factory> _typeFamilyTemplates;
	private static HashMap<Type, HashMap<String, Method>> _typeFamilyMethods;
	
	static
	{
		_primitiveMethods = new HashMap<String, Method>();
		registerPrimitiveMethod(new StringEqualsMethod());
		registerPrimitiveMethod(new StringAddMethod());
		registerPrimitiveMethod(new StringReplaceMethod());
		registerPrimitiveMethod(new StringPrintMethod());
		registerPrimitiveMethod(new BooleanToStringMethod());
		registerPrimitiveMethod(new BooleanNotMethod());
		registerPrimitiveMethod(new BooleanAndMethod());
		registerPrimitiveMethod(new BooleanOrMethod());
		registerPrimitiveMethod(new BooleanXorMethod());
		registerPrimitiveMethod(new IntegerEqualsMethod());
		registerPrimitiveMethod(new IntegerNotEqualsMethod());
		registerPrimitiveMethod(new IntegerGreaterThanMethod());
		registerPrimitiveMethod(new IntegerLessThanMethod());
		registerPrimitiveMethod(new IntegerGreaterThanOrEqualsMethod());
		registerPrimitiveMethod(new IntegerLessThanOrEqualsMethod());
		registerPrimitiveMethod(new IntegerNegateMethod());
		registerPrimitiveMethod(new IntegerAddMethod());
		registerPrimitiveMethod(new IntegerSubMethod());
		registerPrimitiveMethod(new IntegerToStringMethod());

		_typeFamilyTemplates = new HashMap<String, TemplatedMethod.Factory>();
		registerTemplatedMethodFactory(new ArrayResizeMethod.Factory());
		registerTemplatedMethodFactory(new ArraySizeMethod.Factory());
		registerTemplatedMethodFactory(new ArrayGetMethod.Factory());
		registerTemplatedMethodFactory(new ArraySetMethod.Factory());
		
		_typeFamilyMethods = new HashMap<Type, HashMap<String, Method>>();
	}
	
	public static Method lookupPrimitiveMethod(String signature)
	{
		return _primitiveMethods.get(signature);
	}
	
	public static void registerPrimitiveMethod(Method method)
	{
		_primitiveMethods.put(method.getSignature(), method);
	}
	
	public static Method lookupTypeFamilyMethod(Type type, String signature)
	{
		HashMap<String, Method> catalogue = _typeFamilyMethods.get(type);
		if (catalogue == null)
		{
			catalogue = new HashMap<String, Method>();
			_typeFamilyMethods.put(type, catalogue);
		}
		
		Method method = catalogue.get(signature);
		if (method == null)
		{
			TemplatedMethod.Factory f = _typeFamilyTemplates.get(signature);
			if (f == null)
				return null;
			
			method = f.create(type);
			catalogue.put(signature, method);
		}
		
		return method;
	}
	
	public static void registerTemplatedMethodFactory(TemplatedMethod.Factory factory)
	{
		_typeFamilyTemplates.put(factory.getSignature(), factory);
	}
	
	private String _signature;
	private String _identifier;
	private List<Type> _inputTypes;
	private List<Type> _outputTypes;
	
	public Method()
    {
    }

	protected void setSignature(String signature, String identifier)
	{
		_signature = signature;
		_identifier = identifier;
	}
	
	public String getSignature()
	{
		return _signature;
	}
	
	public String getIdentifier()
	{
		return _identifier;
	}
	
	protected void setInputTypes(Type... types)
	{
		_inputTypes = Arrays.asList(types);
	}

	public List<Type> getInputTypes()
	{
		return _inputTypes;
	}
	
	protected void setOutputTypes(Type... types)
	{
		_outputTypes = Arrays.asList(types);
	}

	public List<Type> getOutputTypes()
	{
		return _outputTypes;
	}
	
	private boolean typeCheckImpl(Node node,
				List<Type> methodtypes, List<Type> callertypes)
			throws CompilationException
	{
		if (callertypes.size() != methodtypes.size())
			return false;
		
		try
		{
			for (int i = 0; i < callertypes.size(); i++)
			{
				Type methodtype = methodtypes.get(i);
				Type callertype = callertypes.get(i);
				
				callertype.unifyWith(node, methodtype);
			}
		}
		catch (TypesNotCompatibleException e)
		{
			return false;
		}
		
		return true;
	}

	public void typeCheck(Node node,
			List<Type> outputtypes, List<Type> inputtypes)
		throws CompilationException
	{
		if (((outputtypes != null) && !typeCheckImpl(node, _outputTypes, outputtypes)) ||
			((inputtypes != null) && !typeCheckImpl(node, _inputTypes, inputtypes)))
		{
			throw new MethodParameterMismatch(node, this,
					_outputTypes, outputtypes, _inputTypes, inputtypes);
		}
	}
}
