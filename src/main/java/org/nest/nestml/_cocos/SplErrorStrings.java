/*
 * SplErrorStrings.java
 *
 * This file is part of NEST.
 *
 * Copyright (C) 2004 The NEST Initiative
 *
 * NEST is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * NEST is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NEST.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nest.nestml._cocos;

import de.se_rwth.commons.SourcePosition;
import org.nest.utils.AstUtils;

/**
 * Factory for CoCo error strings. The dispatch is done by the static type of the context condition object.
 * IMPORTANT: Error code must start with the SPL_-prefix
 *
 * @author plotnikov, traeder
 */
public class SplErrorStrings {
  private static final String SEPARATOR = " : ";

  /**
   * Use static methods to get codes and errors
   */
  private SplErrorStrings() {
  }

  static String message(final BlockVariableDefinedMultipleTimes coco, final String variable) {
    final String ERROR_MSG_FORMAT = "The variable %s defined multiple times.";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable);
  }

  @SuppressWarnings({"unused"}) // used for the routing
  static String code(final BlockVariableDefinedMultipleTimes coco) {
    return "SPL_VARIABLE_EXISTS_MULTIPLE_TIMES";
  }

  static String message(final VariableHasTypeName coco, final String variable) {
    final String ERROR_MSG_FORMAT = "Variable '%s' has name of an existing NESTML type.";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable);
  }

  @SuppressWarnings({"unused"}) // used for the routing
  public static String code(final VariableHasTypeName coco) {
    return "SPL_VARIABLE_HAS_TYPE_NAME";
  }

  static String messageDefinedBeforeUse(
      final VariableNotDefinedBeforeUse coco,
      final String variable,
      final SourcePosition previousDefinition) {
    final String ERROR_MSG_FORMAT = "Variable '%s' not defined yet. It is defined at line '%s'";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable, AstUtils.print(previousDefinition));
  }

  static String messageDefinedBeforeUse(
      final VariableNotDefinedBeforeUse coco,
      final String variable) {
    final String ERROR_MSG_FORMAT = "Cannot use variable '%s' before its definition.";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable);
  }

  static String messageOwnAssignment(
      final VariableNotDefinedBeforeUse coco,
      final String variable) {
    final String ERROR_MSG_FORMAT = "Cannot use variable '%s' in the assignment of its own declaration.";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable);
  }

  @SuppressWarnings({"unused"}) // used for the routing
  public static String code(final VariableNotDefinedBeforeUse coco) {
    return "SPL_VARIABLE_NOT_DEFINED_BEFORE_USE";
  }


  static String messageInitType(
      final IllegalExpression coco,
      final String variable,
      final String varType,
      final String expressionType) {
    final String ERROR_MSG_FORMAT = "Attempting to initialize variable %s of type %s with an expression of type %s" ;

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable, varType, expressionType);
  }

  static String messageAssignment(
      final IllegalExpression coco,
      final String variable,
      final String varType,
      final String expressionType) {
    final String ERROR_MSG_FORMAT = "Attempting to assign %s to variable %s with type %s" ;

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT,expressionType, variable, varType);
  }

  static String messageNonBoolean(
      final IllegalExpression coco,
      final String expressionType) {
    final String ERROR_MSG_FORMAT = "Cannot use non boolean expression of type %s";

    return code(coco) + SEPARATOR+ String.format(ERROR_MSG_FORMAT, expressionType);
  }

  static String messageCastToReal(
      final IllegalExpression coco,
      final String soruceType){
    final String ERROR_MSG_FORMAT = "Implicit cast from %s to real";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, soruceType);
  }

  static String messageForLoop(
      final IllegalExpression coco,
      final String variable,
      final String type){
    final String ERROR_MSG_FORMAT = "The type of the iterator variable %s in a for-loop must be numeric and not:" +
                                    " '%s' .";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable, type);
  }

  static String messageForLoopBound(
      final IllegalExpression coco,
      final String variable,
      final String type) {
    final String ERROR_MSG_FORMAT = "The type of the loop bound must be a numeric type. The value of the current bound "
                                    + "%s is %s .";

    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, variable, type);
  }
  @SuppressWarnings({"unused"}) // used for the routing
  public static String code(final IllegalExpression coco) {
    return "SPL_ILLEGAL_EXPRESSION";
  }


  static String message(
      final CodeAfterReturn coco,
      final String errorDescription) {
    return code(coco) + SEPARATOR + errorDescription;
  }


  @SuppressWarnings({"unused"}) // used for the routing
  public static String code(final CodeAfterReturn coco) {
    return "SPL_CODE_AFTER_RETURN";
  }

  static String message(
      final FunctionDoesNotExist coco,
      final String functionName,
      final String signature) {

    final String ERROR_MSG_FORMAT = "The function '%s' with the signature '%s' is not defined.";
    return code(coco) + SEPARATOR + String.format(ERROR_MSG_FORMAT, functionName, signature.isEmpty()?"()":signature);
  }


  @SuppressWarnings({"unused"}) // used for the routing
  public static String code(final FunctionDoesNotExist coco) {
    return "SPL_FUNCTION_DOES_NOT_EXIST";
  }

}
