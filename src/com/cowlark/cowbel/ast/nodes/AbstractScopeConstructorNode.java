/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.ast.nodes;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.cowlark.cowbel.Compiler;
import com.cowlark.cowbel.Constructor;
import com.cowlark.cowbel.Function;
import com.cowlark.cowbel.FunctionTemplate;
import com.cowlark.cowbel.Label;
import com.cowlark.cowbel.ast.HasIdentifier;
import com.cowlark.cowbel.ast.HasInputs;
import com.cowlark.cowbel.ast.HasTypeArguments;
import com.cowlark.cowbel.errors.AmbiguousVariableReference;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.errors.IdentifierNotFound;
import com.cowlark.cowbel.errors.MultipleDefinitionException;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.symbols.Symbol;

public abstract class AbstractScopeConstructorNode extends AbstractStatementNode
{
	private TreeSet<Symbol> _importedSymbols = new TreeSet<Symbol>();
	private TreeMap<Symbol, AbstractScopeConstructorNode> _exportedSymbols = new TreeMap<Symbol, AbstractScopeConstructorNode>();
	private TreeSet<Function> _importedFunctions = new TreeSet<Function>();
	private TreeMap<Function, AbstractScopeConstructorNode> _exportedFunctions = new TreeMap<Function, AbstractScopeConstructorNode>();
	private TreeSet<AbstractScopeConstructorNode> _importedScopes = new TreeSet<AbstractScopeConstructorNode>();
	private TreeSet<AbstractScopeConstructorNode> _exportedScopes = new TreeSet<AbstractScopeConstructorNode>();
	private TreeSet<Symbol> _symbols = new TreeSet<Symbol>();
	private TreeSet<FunctionTemplate> _functionTemplates = new TreeSet<FunctionTemplate>();
	private TreeMap<String, Label> _labels = new TreeMap<String, Label>();
	private Function _function;
	private Constructor _constructor;
	
	public AbstractScopeConstructorNode(Location start, Location end)
    {
        super(start, end);
    }

	public AbstractScopeConstructorNode(Location start, Location end, AbstractStatementNode child)
    {
        super(start, end);
        addChild(child);
    }

	public AbstractStatementNode getChild()
	{
		return (AbstractStatementNode) getChild(0);
	}
	
	public boolean isFunctionScope()
	{
		return false;
	}
	
	public Function getFunction()
	{
		return _function;
	}
	
	public void setFunction(Function function)
	{
		_function = function;
	}
	
	public Constructor getConstructor()
	{
		return _constructor;
	}
	
	public void setConstructor(Constructor constructor)
	{
		_constructor = constructor;
	}
	
	public Set<Symbol> getSymbols()
	{
		return _symbols;
	}
	
	public Set<FunctionTemplate> getFunctionTemplates()
	{
		return _functionTemplates;
	}
	
	public Set<Label> getLabels()
	{
		return new TreeSet<Label>(_labels.values());
	}
	
	public Set<AbstractScopeConstructorNode> getImportedScopes()
	{
		return _importedScopes;
	}
	
	public Set<AbstractScopeConstructorNode> getExportedScopes()
	{
		return _exportedScopes;
	}
	
	@Override
	public String getShortDescription()
	{
		return "";
	}
	
	@Override
	public void dumpDetails(int indent)
	{
		if (_function != null)
		{
			spaces(indent);
			System.out.print("function: ");
			System.out.println(_function.toString());
		}
		
		if (_constructor != null)
		{
			spaces(indent);
			System.out.print("stack frame: ");
			System.out.println(_constructor.toString());
		}
		
		if (!_symbols.isEmpty())
		{
			spaces(indent);
			System.out.println("defined here:");
			for (Symbol s: _symbols)
			{
				spaces(indent+1);
				System.out.println(s.toString());
			}
		}
		
		if (!_exportedSymbols.isEmpty())
		{
			spaces(indent);
			System.out.println("exported symbols:");
			for (Symbol e : _exportedSymbols.keySet())
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_exportedFunctions.isEmpty())
		{
			spaces(indent);
			System.out.println("exported functions:");
			for (Function e : _exportedFunctions.keySet())
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_exportedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("exported scopes:");
			for (AbstractScopeConstructorNode e : _exportedScopes)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedSymbols.isEmpty())
		{
			spaces(indent);
			System.out.println("imported symbols:");
			for (Symbol e: _importedSymbols)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedFunctions.isEmpty())
		{
			spaces(indent);
			System.out.println("imported functions:");
			for (Function e: _importedFunctions)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
		
		if (!_importedScopes.isEmpty())
		{
			spaces(indent);
			System.out.println("imported scopes:");
			for (AbstractScopeConstructorNode e: _importedScopes)
			{
				spaces(indent+1);
				System.out.println(e.toString());
			}
		}
	}
	
	private FunctionScopeConstructorNode _functionScope;
	public FunctionScopeConstructorNode getFunctionScope()
	{
		if (_functionScope == null)
			_functionScope = getScope().getFunctionScope();
		return _functionScope;
	}
	
	public void addSymbol(Symbol symbol) throws CompilationException
	{
		String symbolname = symbol.getName();
		
		for (Symbol s : _symbols)
		{
			if (s.getName().equals(symbolname))
				throw new MultipleDefinitionException(s, symbol);
		}
		
		for (FunctionTemplate ft : _functionTemplates)
		{
			if (ft.getName().equals(symbolname))
				throw new MultipleDefinitionException(ft, symbol);
		}
		
		_symbols.add(symbol);
	}
	
	/* Recurse all the way to the top. */
	public Symbol lookupVariable(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		TreeSet<Symbol> results = new TreeSet<Symbol>();
		
		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			results.clear();
			
			for (Symbol symbol : scope.getSymbols())
			{
				if (symbol.getName().equals(s))
					results.add(symbol);
			}
			
			switch (results.size())
			{
				case 1:
					return results.first();
					
				default:
					throw new AmbiguousVariableReference(this, name, results);
					
				case 0:
					break;
			}
				
			scope = scope.getScope();
		}
				
		throw new IdentifierNotFound(this, name);
	}

	public void addFunctionTemplate(FunctionTemplate template)
			throws CompilationException
	{
		String name = template.getName();
		for (Symbol s : _symbols)
		{
			if (s.getName().equals(name))
				throw new MultipleDefinitionException(s, template);
		}
		
		String signature = template.getSignature();
		for (FunctionTemplate ft : _functionTemplates)
		{
			if (ft.getSignature().equals(signature))
				throw new MultipleDefinitionException(ft, template);
		}
		
		_functionTemplates.add(template);
	}
	
	/* Recurse all the way to the top. */
	public <T extends Node & HasInputs & HasIdentifier & HasTypeArguments>
		Function lookupFunction(T node)
			throws CompilationException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(node.getIdentifier().getText());
		sb.append("<");
		sb.append(node.getTypeArguments().getNumberOfChildren());
		sb.append(">(");
		sb.append(node.getInputs().getNumberOfChildren());
		sb.append(")");
		String signature = sb.toString();

		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			for (FunctionTemplate ft : scope.getFunctionTemplates())
			{
				if (ft.getSignature().equals(signature))
				{
					return Compiler.Instance.getFunctionInstance(
							node, node.getTypeArguments(), ft);
				}
			}
				
			scope = scope.getScope();
		}
				
		return null;
	}

	private void recursive_import(AbstractScopeConstructorNode root, AbstractScopeConstructorNode leaf)
	{
		if (root == leaf)
			return;
		
		AbstractScopeConstructorNode n = leaf;
		for (;;)
		{
			n._importedScopes.add(root);
			root._exportedScopes.add(n);
			
			if (n.getScope() == root)
				break;
			n = n.getScope();
		}
		
		recursive_import(n, leaf);
	}
	
	public void importSymbol(Symbol symbol)
	{
		AbstractScopeConstructorNode symscope = symbol.getScope();
		AbstractScopeConstructorNode thisscope = this;
		
		/* No need to import symbols from the current scope. */
		
		if (symscope == thisscope)
			return;
		
		_importedSymbols.add(symbol);
		symscope._exportedSymbols.put(symbol, this);
		
		recursive_import(symscope, thisscope);
	}
	
	public void importFunction(Function function)
	{
		AbstractScopeConstructorNode funcscope = function.getScope();
		AbstractScopeConstructorNode thisscope = this;
		
		/* No need to import symbols from the current scope. */
		
		if (funcscope == thisscope)
			return;
		
		_importedFunctions.add(function);
		funcscope._exportedFunctions.put(function, this);
		
		recursive_import(funcscope, thisscope);
	}
	
	public void addLabel(Label label) throws CompilationException
	{
		IdentifierNode name = label.getLabelName();
		String s = name.getText();
		
		if (_labels.containsKey(s))
		{
			Label oldlabel = _labels.get(s);
			throw new MultipleDefinitionException(oldlabel, label);
		}
		
		_labels.put(s, label);
	}
	
	/* Recurse only up to the next function scope. */
	public Label lookupLabel(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		
		AbstractScopeConstructorNode scope = this;
		while (scope != null)
		{
			Label label = scope._labels.get(s);
			if (label != null)
				return label;
			
			if (scope.isFunctionScope())
				break;
			
			scope = scope.getScope();
		}
		
		throw new IdentifierNotFound(this, name);
	}
	
	@Override
	public boolean isLoopingNode()
	{
		return !_labels.isEmpty();
	}
	
	public boolean isSymbolExportedToDifferentFunction(Symbol sym)
	{
		AbstractScopeConstructorNode s = _exportedSymbols.get(sym);
		if (s == null)
			return false;
		
		Function f = s.getFunction();
		return (f != _function);
	}
}