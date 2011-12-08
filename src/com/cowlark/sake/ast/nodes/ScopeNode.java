package com.cowlark.sake.ast.nodes;

import java.util.HashMap;
import java.util.Map.Entry;
import com.cowlark.sake.Symbol;
import com.cowlark.sake.SymbolStorage;
import com.cowlark.sake.ast.Visitor;
import com.cowlark.sake.errors.CompilationException;
import com.cowlark.sake.errors.MultipleDefinitionException;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.errors.IdentifierNotFound;

public class ScopeNode extends StatementNode
{
	private SymbolStorage _symbolStorage;
	private HashMap<String, Symbol> _symbols = new HashMap<String, Symbol>();
	
	public ScopeNode(Location start, Location end, StatementNode child)
    {
        super(start, end);
        addChild(child);
    }
	
	public StatementNode getChild()
	{
		return (StatementNode) getChild(0);
	}
	
	@Override
	public void dumpDetails(int indent)
	{
		for (Entry<String, Symbol> e: _symbols.entrySet())
		{
			spaces(indent);
			System.out.println(e.getValue().toString());
		}
	}
	
	@Override
	public String getShortDescription()
	{
	    return "using storage " + getSymbolStorage();
	}
	
	@Override
	public void visit(Visitor visitor) throws CompilationException
	{
		visitor.visit(this);
	}
	
	public void setSymbolStorage(SymbolStorage storage)
	{
		_symbolStorage = storage;
	}
	
	public SymbolStorage getSymbolStorage()
	{
		if (_symbolStorage == null)
			_symbolStorage = getScope().getSymbolStorage();
		
		return _symbolStorage;
	}
	
	public void addSymbol(Symbol symbol) throws CompilationException
	{
		IdentifierNode name = symbol.getSymbolName();
		String s = name.getText();
		
		if (_symbols.containsKey(s))
		{
			Symbol oldsymbol = _symbols.get(s);
			throw new MultipleDefinitionException(oldsymbol.getSymbolName(), name);
		}
		
		_symbols.put(s, symbol);
		
		SymbolStorage storage = getSymbolStorage();
		storage.addSymbol(symbol);
		symbol.setScopeAndStorage(this, storage);
	}
	
	public Symbol lookupSymbol(IdentifierNode name) throws CompilationException
	{
		String s = name.getText();
		
		ScopeNode scope = this;
		while (scope != null)
		{
			Symbol symbol = scope._symbols.get(s);
			if (symbol != null)
				return symbol;
			
			scope = scope.getScope();
		}
		
		throw new IdentifierNotFound(this, name);
	}
}