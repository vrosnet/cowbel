package com.cowlark.sake;

import com.cowlark.sake.ast.RecursiveVisitor;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ParameterDeclarationListNode;
import com.cowlark.sake.ast.nodes.ParameterDeclarationNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.errors.CompilationException;

public class RecordVariableDeclarationsVisitor extends RecursiveVisitor
{
	private Node _rootNode;
	
	public RecordVariableDeclarationsVisitor(Node rootNode)
    {
		_rootNode = rootNode;
    }
	
	@Override
	public void visit(FunctionDefinitionNode node)
	        throws CompilationException
	{
		/* Add this function to the current scope. */
		
		Function f = new Function(node);
		node.getScope().addSymbol(f);
		node.setSymbol(f);
		
		/* Set up the function definition's scope and storage. */
		
		ScopeNode body = node.getFunctionBody();
		LocalSymbolStorage storage = new LocalSymbolStorage();
		body.setSymbolStorage(storage);
		
		/* Add function parameters to its scope. */
		
		ParameterDeclarationListNode pdln = node.getFunctionHeader().getParametersNode();
		for (Node n : pdln.getChildren())
		{
			ParameterDeclarationNode pdn = (ParameterDeclarationNode) n;

			Variable v = new LocalVariable(pdn);
			body.addSymbol(v);
			pdn.setSymbol(v);
		}

		super.visit(node);
	}
	
	@Override
	public void visit(VarDeclarationNode node)
	        throws CompilationException
	{
		/* Add this symbol to the current scope. */
		
		ScopeNode scope = node.getScope();
		Variable v;
		if (scope == _rootNode)
			v = new GlobalVariable(node);
		else
			v = new LocalVariable(node);
		
		scope.addSymbol(v);
		node.setSymbol(v);
		
	    super.visit(node);
	}
}