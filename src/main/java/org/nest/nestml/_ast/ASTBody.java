/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package org.nest.nestml._ast;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.monticore.symboltable.Scope;
import org.nest.commons._ast.ASTBLOCK_CLOSE;
import org.nest.commons._ast.ASTBLOCK_OPEN;
import org.nest.commons._ast.ASTExpr;
import org.nest.ode._ast.ASTOdeDeclaration;
import org.nest.symboltable.symbols.VariableSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Provides convenient  functions to statically type interfaces astnodes resulting from the Body-grammar
 * production.
 *
 * @author plotnikov
 */
public class ASTBody extends ASTBodyTOP {

  public ASTBody() {
    // this constructor is used in the generated code and must be provided
  }

  public ASTBody(
      final ASTBLOCK_OPEN bLOCK_open,
      final List<String> nEWLINEs,
      final List<ASTBodyElement> bodyElements,
      final ASTBLOCK_CLOSE bLOCK_close) {
    super(bLOCK_open, nEWLINEs, bodyElements, bLOCK_close);
  }

  // Retrieves model structure blocks
  public List<ASTFunction> getFunctions() {
    List<ASTFunction> result = this.getBodyElements().stream()
        .filter(be -> be instanceof ASTFunction)
        .map(be -> (ASTFunction) be)
        .collect(Collectors.toList());

    return ImmutableList.copyOf(result);
  }

  public List<ASTDynamics> getDynamics() {
    List<ASTDynamics> result = this.getBodyElements().stream()
        .filter(be -> be instanceof ASTDynamics)
        .map(be -> (ASTDynamics) be)
        .collect(Collectors.toList());

    return ImmutableList.copyOf(result);
  }

  public Optional<ASTBodyElement> getStateBlock() {
    return this.getBodyElements().stream()
        .filter(be -> be instanceof ASTVar_Block && ((ASTVar_Block) be).isState())
        .findFirst(); // there is at most one
  }

  public List<ASTAliasDecl> getStateDeclarations() {
    final Optional<ASTBodyElement> stateBlock = getStateBlock();
    final List<ASTAliasDecl> result = Lists.newArrayList();
    stateBlock.ifPresent(block -> result.addAll( ((ASTVar_Block) block).getAliasDecls()));
    return result;
  }

  public Optional<ASTBodyElement> getParameterBlock() {
    return this.getBodyElements().stream()
        .filter(be -> be instanceof ASTVar_Block && ((ASTVar_Block) be).isParameter())
        .findFirst(); // there is at most one
  }

  public List<ASTAliasDecl> getParameterDeclarations() {
    final Optional<ASTBodyElement> stateBlock = getParameterBlock();
    final List<ASTAliasDecl> result = Lists.newArrayList();
    stateBlock.ifPresent(block -> result.addAll( ((ASTVar_Block) block).getAliasDecls()));
    return result;
  }

  public Optional<ASTBodyElement> getInternalBlock() {
    return this.getBodyElements().stream()
        .filter(be -> be instanceof ASTVar_Block && ((ASTVar_Block) be).isInternal())
        .findFirst(); // there is at most one
  }

  public List<ASTAliasDecl> getInternalDeclarations() {
    final Optional<ASTBodyElement> stateBlock = getInternalBlock();
    final List<ASTAliasDecl> result = Lists.newArrayList();
    stateBlock.ifPresent(block -> result.addAll( ((ASTVar_Block) block).getAliasDecls()));
    return result;
  }


  public Optional<ASTOdeDeclaration> getEquations() {
    final Optional<ASTEquations> equations = findEquationsBlock();
    if (equations.isPresent()) {
      return Optional.of(equations.get().getOdeDeclaration());
    }
    else {
      return Optional.empty();
    }
  }

  private Optional<ASTEquations> findEquationsBlock() {
    final Optional<ASTBodyElement> equations = this.getBodyElements()
        .stream()
        .filter(be -> be instanceof ASTEquations)
        .findFirst();
    if (equations.isPresent()) {
      // only ASTEquations are filtered
      return Optional.of((ASTEquations) equations.get());
    }
    else {
      return Optional.empty();
    }
  }

  // STATE Block handling
  public List<VariableSymbol> getStateSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isState), getEnclosingScope().get());
  }

  public List<VariableSymbol> getStateAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isState), getEnclosingScope().get())
        .stream()
        .filter(VariableSymbol::isAlias)
        .collect(Collectors.toList());
  }

  public List<VariableSymbol> getStateNonAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isState), getEnclosingScope().get())
        .stream()
        .filter(variable -> !variable.isAlias())
        .collect(Collectors.toList());
  }

  // Parameter Block handling
  public List<VariableSymbol> getParameterSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isParameter), getEnclosingScope().get());
  }

  public List<VariableSymbol> getParameterAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isParameter), getEnclosingScope().get())
        .stream()
        .filter(VariableSymbol::isAlias)
        .collect(Collectors.toList());
  }

  public List<VariableSymbol> getParameterNonAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isParameter), getEnclosingScope().get())
        .stream()
        .filter(variable -> !variable.isAlias())
        .collect(Collectors.toList());
  }

  // Internal Block handling
  public List<VariableSymbol> getInternalSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isInternal), getEnclosingScope().get());
  }

  public List<VariableSymbol> getInternalAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isInternal), getEnclosingScope().get())
        .stream()
        .filter(VariableSymbol::isAlias)
        .collect(Collectors.toList());
  }

  public List<VariableSymbol> getInternalNonAliasSymbols() {
    return getVariableSymbols(getDeclarationsFromBlock(ASTVar_Block::isInternal), getEnclosingScope().get())
        .stream()
        .filter(variable -> !variable.isAlias())
        .collect(Collectors.toList());
  }

  private List<ASTAliasDecl> getDeclarationsFromBlock(final Predicate<ASTVar_Block> predicate) {
    final List<ASTAliasDecl> result = Lists.newArrayList();

    this.getBodyElements().stream().filter(be -> be instanceof ASTVar_Block).forEach(be -> {
      ASTVar_Block block = (ASTVar_Block) be;
      if (predicate.test(block)) {
        result.addAll(block.getAliasDecls());
      }
    });

    return result;
  }

  private List<VariableSymbol> getVariableSymbols(
      final List<ASTAliasDecl> aliasDeclarations,
      final Scope scope) {
    return aliasDeclarations.stream()
        .flatMap(alias -> alias.getDeclaration().getVars().stream()) // get all variables form the declaration
        .map(variable -> {
          Optional<VariableSymbol> varSymbol = scope.resolve(variable, VariableSymbol.KIND);
          return varSymbol.get(); // assumes the all condition are fullfiled and the variable exists
        })
        .collect(toList());
  }


  @SuppressWarnings("unused") // used in templates
  public List<ASTExpr> getParameterInvariants() {
    return getParameterDeclarations().stream()
        .filter(param -> param.getInvariant().isPresent())
        .map(param -> param.getInvariant().get()) // ensured by the filter function
        .collect(toList());
  }

  public void addToInternalBlock(final ASTAliasDecl astAliasDecl) {
    this.getBodyElements().stream().filter(variableBlock -> variableBlock instanceof ASTVar_Block).forEach(be -> {

      ASTVar_Block block = (ASTVar_Block) be;

      if (block.isInternal()) {
        block.getAliasDecls().add(astAliasDecl);
      }

    });

  }

  public void addToStateBlock(final ASTAliasDecl astAliasDecl) {
    this.getBodyElements().stream().filter(variableBlock -> variableBlock instanceof ASTVar_Block).forEach(be -> {

      ASTVar_Block block = (ASTVar_Block) be;

      if (block.isState()) {
        block.getAliasDecls().add(astAliasDecl);
      }

    });

  }

  private Optional<ASTBodyElement> findDynamics() {
    return this.getBodyElements().stream()
          .filter(be -> be instanceof ASTDynamics)
          .findFirst();
  }

  public List<ASTInputLine> getInputLines() {
    List<ASTInputLine> result = new ArrayList<ASTInputLine>();

    for (ASTBodyElement be : this.getBodyElements()) {
      if (be instanceof ASTInput) {
        ASTInput in = (ASTInput) be;
        for (ASTInputLine inline : in.getInputLines()) {
          result.add(inline);
        }
      }
    }

    return ImmutableList.copyOf(result);
  }

  public List<ASTOutput> getOutputs() {
    List<ASTOutput> result = this.getBodyElements().stream()
        .filter(be -> be instanceof ASTOutput)
        .map(be -> (ASTOutput) be)
        .collect(Collectors.toList());

    return ImmutableList.copyOf(result);
  }

  public List<ASTStructureLine> getStructure() {
    List<ASTStructureLine> result = new ArrayList<ASTStructureLine>();

    for (ASTBodyElement be : this.getBodyElements()) {
      if (be instanceof ASTStructure) {
        ASTStructure st = (ASTStructure) be;
        for (ASTStructureLine stline : st.getStructureLines()) {
          result.add(stline);
        }
      }
    }

    return ImmutableList.copyOf(result);
  }


}
