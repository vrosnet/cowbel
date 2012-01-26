package com.cowlark.cowbel;

import com.cowlark.cowbel.ast.RecursiveVisitor;
import com.cowlark.cowbel.ast.nodes.ScopeConstructorNode;
import com.cowlark.cowbel.errors.CompilationException;
import com.cowlark.cowbel.symbols.Symbol;

public class AssignVariablesToConstructorsVisitor extends RecursiveVisitor
{
	@Override
	public void visit(ScopeConstructorNode node) throws CompilationException
	{
		Constructor sf = node.getConstructor();
		for (Symbol s : node.getSymbols())
			s.addToConstructor(sf);

		for (ScopeConstructorNode s : node.getImportedScopes())
			sf.addConstructor(s.getConstructor());
		
		super.visit(node);
	}
}
