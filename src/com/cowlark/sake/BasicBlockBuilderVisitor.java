package com.cowlark.sake;

import java.util.List;
import com.cowlark.sake.ast.SimpleVisitor;
import com.cowlark.sake.ast.nodes.DummyExpressionNode;
import com.cowlark.sake.ast.nodes.ExpressionNode;
import com.cowlark.sake.ast.nodes.ExpressionStatementNode;
import com.cowlark.sake.ast.nodes.FunctionCallNode;
import com.cowlark.sake.ast.nodes.FunctionDefinitionNode;
import com.cowlark.sake.ast.nodes.GotoStatementNode;
import com.cowlark.sake.ast.nodes.IfElseStatementNode;
import com.cowlark.sake.ast.nodes.IfStatementNode;
import com.cowlark.sake.ast.nodes.LabelStatementNode;
import com.cowlark.sake.ast.nodes.ListConstructorNode;
import com.cowlark.sake.ast.nodes.MethodCallNode;
import com.cowlark.sake.ast.nodes.Node;
import com.cowlark.sake.ast.nodes.ReturnStatementNode;
import com.cowlark.sake.ast.nodes.ReturnVoidStatementNode;
import com.cowlark.sake.ast.nodes.ScopeNode;
import com.cowlark.sake.ast.nodes.StringConstantNode;
import com.cowlark.sake.ast.nodes.VarAssignmentNode;
import com.cowlark.sake.ast.nodes.VarDeclarationNode;
import com.cowlark.sake.ast.nodes.VarReferenceNode;
import com.cowlark.sake.errors.CompilationException;

public class BasicBlockBuilderVisitor extends SimpleVisitor
{
	private Function _function;
	private BasicBlock _currentBB;
	
	public BasicBlockBuilderVisitor(Function function)
    {
		_function = function;
		_currentBB = function.getEntryBB();
    }
	
	public BasicBlock getCurrentBasicBlock()
	{
		return _currentBB;
	}
	
	@Override
	public void visit(FunctionDefinitionNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(ScopeNode node) throws CompilationException
	{
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(VarDeclarationNode node) throws CompilationException
	{
	}
	
	@Override
	public void visit(VarAssignmentNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		if (symbol instanceof GlobalVariable)
		{
			_currentBB.insnSetGlobalVariable(node, (GlobalVariable) symbol);
			node.getExpression().visit(this);
		}
		else
		{
			BasicBlock nextbb = new BasicBlock();
			_currentBB.insnSetLocalVariableIn(node, (LocalVariable) symbol, nextbb);
			node.getExpression().visit(this);
			_currentBB = nextbb;
		}

	}
	
	@Override
	public void visit(ReturnStatementNode node) throws CompilationException
	{
		_currentBB.insnSetReturnValue(node);
		_currentBB.insnGoto(node, _function.getExitBB());
		node.getValue().visit(this);
	}
	
	@Override
	public void visit(ReturnVoidStatementNode node) throws CompilationException
	{
		_currentBB.insnGoto(node, _function.getExitBB());
	}
	
	@Override
	public void visit(IfStatementNode node) throws CompilationException
	{
		BasicBlock conditionalbb = _currentBB;
		BasicBlock positivebb = new BasicBlock();
		BasicBlock exitbb = new BasicBlock();
		
		_currentBB = conditionalbb;
		_currentBB.insnIf(node, positivebb, exitbb);
		node.getConditionalExpression().visit(this);
		
		_currentBB = positivebb;
		node.getPositiveStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		
		_currentBB = exitbb;
	}
	
	@Override
	public void visit(IfElseStatementNode node) throws CompilationException
	{
		BasicBlock conditionalbb = _currentBB;
		BasicBlock positivebb = new BasicBlock();
		BasicBlock negativebb = new BasicBlock();
		BasicBlock exitbb = new BasicBlock();
		
		_currentBB = conditionalbb;
		_currentBB.insnIf(node, positivebb, negativebb);
		node.getConditionalExpression().visit(this);
		
		_currentBB = positivebb;
		node.getPositiveStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		
		_currentBB = negativebb;
		node.getNegativeStatement().visit(this);
		_currentBB.insnGoto(node, exitbb);
		
		_currentBB = exitbb;
	}
	
	@Override
	public void visit(LabelStatementNode node) throws CompilationException
	{
		Label label = node.getLabel();
		BasicBlock next = label.getBasicBlock();
		
		_currentBB.insnGoto(node, next);
		_currentBB = next;
	}
	
	@Override
	public void visit(GotoStatementNode node) throws CompilationException
	{
		Label label = node.getLabel();
		BasicBlock next = label.getBasicBlock();
		
		_currentBB.insnGoto(node, next);
	}
	
	@Override
	public void visit(ExpressionStatementNode node) throws CompilationException
	{
		_currentBB.insnDiscard(node);
		node.getExpression().visit(this);
	}
	
	@Override
	public void visit(FunctionCallNode node) throws CompilationException
	{
		List<ExpressionNode> arguments = node.getArguments();
		
		_currentBB.insnFunctionCall(node, arguments.size());
		node.getFunction().visit(this);
		for (Node n : arguments)
			n.visit(this);		
	}
	
	@Override
	public void visit(MethodCallNode node) throws CompilationException
	{
		List<ExpressionNode> arguments = node.getMethodArguments();

		_currentBB.insnMethodCall(node, node.getMethodIdentifier(), arguments.size());
		node.getMethodReceiver().visit(this);
		for (Node n : arguments)
			n.visit(this);
	}
	
	@Override
	public void visit(DummyExpressionNode node) throws CompilationException
	{
		node.getChild().visit(this);
	}
	
	@Override
	public void visit(VarReferenceNode node) throws CompilationException
	{
		Symbol symbol = node.getSymbol();
		if (symbol instanceof GlobalVariable)
			_currentBB.insnGetGlobalVariable(node, (GlobalVariable) symbol);
		else
			_currentBB.insnGetLocalVariable(node, (LocalVariable) symbol);
	}
	
	@Override
	public void visit(ListConstructorNode node) throws CompilationException
	{
		List<ExpressionNode> members = node.getListMembers();
		_currentBB.insnListConstructor(node, members.size());
		
		for (ExpressionNode expr : members)
			expr.visit(this);
	}
	
	@Override
	public void visit(StringConstantNode node) throws CompilationException
	{
		_currentBB.insnStringConstant(node, node.getText());
	}
	
	@Override
	public void visit(Node node) throws CompilationException
	{
		assert(false);
	}
}