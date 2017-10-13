package de.monticore.lang.monticar.generator.cpp.converter;

import de.monticore.lang.math.math._symboltable.MathForLoopHeadSymbol;
import de.monticore.lang.math.math._symboltable.expression.*;
import de.monticore.lang.math.math._symboltable.matrix.*;
import de.monticore.lang.monticar.generator.Variable;
import de.monticore.lang.monticar.generator.cpp.MathFunctionFixer;
import de.monticore.lang.monticar.generator.cpp.OctaveHelper;
import de.monticore.lang.monticar.generator.cpp.symbols.MathChainedExpression;
import de.monticore.lang.monticar.generator.cpp.symbols.MathStringExpression;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sascha Schneiders
 */
public class ExecuteMethodGeneratorHandler {

    public static String generateExecuteCode(MathParenthesisExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        String result = "";
        result += "(";
        result += ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol.getMathExpressionSymbol(), includeStrings);
        result += ")";
        return result;
    }

    public static String generateExecuteCode(MathChainedExpression mathChainedExpression, List<String> includeStrings) {
        String result = "";
        result += ExecuteMethodGenerator.generateExecuteCode(mathChainedExpression.getFirstExpressionSymbol(), includeStrings);
        result += ExecuteMethodGenerator.generateExecuteCode(mathChainedExpression.getSecondExpressionSymbol(), includeStrings);
        return result;
    }

    public static String generateExecuteCode(MathStringExpression mathExpressionSymbol, List<String> includeStrings) {
        String result = "";
        result += mathExpressionSymbol.getTextualRepresentation();
        return result;
    }

    public static String generateExecuteCode(MathPreOperatorExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        String result = "";
        result += mathExpressionSymbol.getOperator() + ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol.getMathExpressionSymbol(), includeStrings);
        return result;
    }

    public static String generateExecuteCode(MathCompareExpressionSymbol mathCompareExpressionSymbol, List<String> includeStrings) {
        String result = "";
        result += "(" + ExecuteMethodGenerator.generateExecuteCode(mathCompareExpressionSymbol.getLeftExpression(), includeStrings) + " " + mathCompareExpressionSymbol.getCompareOperator();
        result += " " + ExecuteMethodGenerator.generateExecuteCode(mathCompareExpressionSymbol.getRightExpression(), includeStrings) + ")";

        return result;
    }

    public static String generateExecuteCode(MathValueExpressionSymbol mathValueExpressionSymbol, List<String> includeStrings) {
        if (mathValueExpressionSymbol.isNameExpression()) {
            return generateExecuteCode((MathNameExpressionSymbol) mathValueExpressionSymbol, includeStrings);
        } else if (mathValueExpressionSymbol.isNumberExpression()) {
            return generateExecuteCode((MathNumberExpressionSymbol) mathValueExpressionSymbol, includeStrings);
        } else if (mathValueExpressionSymbol.isAssignmentDeclarationExpression()) {
            return generateExecuteCodeDeclaration((MathValueSymbol) mathValueExpressionSymbol, includeStrings);
        } else {
            Log.error("0xMAVAEXSY Case not handled!");
        }
        return null;
    }

    public static String generateExecuteCodeDeclaration(MathValueSymbol mathValueSymbol, List<String> includeStrings) {
        String result = "";
        List<String> properties = mathValueSymbol.getType().getProperties();
        if (properties.contains("static")) {
            Variable var = new Variable(mathValueSymbol.getName(), Variable.STATIC);
            var.setTypeNameTargetLanguage(TypeConverter.getVariableTypeNameForMathLanguageTypeName(mathValueSymbol.getType()));
            for (MathExpressionSymbol dimension : mathValueSymbol.getType().getDimensions())
                var.addDimensionalInformation(dimension.getTextualRepresentation());

            ComponentConverter.currentBluePrint.addVariable(var);
        } else {
            result += generateExecuteCode(mathValueSymbol.getType(), includeStrings);
            result += " " + mathValueSymbol.getName();
            if (mathValueSymbol.getValue() != null) {
                result += " = " + ExecuteMethodGenerator.generateExecuteCode(mathValueSymbol.getValue(), includeStrings);
            }
            result += ";\n";
        }
        ComponentConverter.currentBluePrint.getMathInformationRegister().addVariable(mathValueSymbol);
        //result += mathValueSymbol.getTextualRepresentation();
        return result;
    }


    public static String generateExecuteCode(MathValueSymbol mathValueSymbol, List<String> includeStrings) {
        String result = "";

        result += generateExecuteCode(mathValueSymbol.getType(), includeStrings);
        result += " " + mathValueSymbol.getName();
        if (mathValueSymbol.getValue() != null) {
            result += " = " + ExecuteMethodGenerator.generateExecuteCode(mathValueSymbol.getValue(), includeStrings);
            result += ";\n";
        }
        //result += mathValueSymbol.getTextualRepresentation();
        return result;
    }

    public static String generateExecuteCode(MathValueType mathValueType, List<String> includeStrings) {
        String result = "";
        if (mathValueType.isRationalType()) {
            if (mathValueType.getDimensions().size() == 0)
                return "double";
            else if (mathValueType.getDimensions().size() == 2) {
                Log.info("Dim1:" + mathValueType.getDimensions().get(0).getTextualRepresentation() + "Dim2: " + mathValueType.getDimensions().get(1).getTextualRepresentation(), "DIMS:");
                if (mathValueType.getDimensions().get(0).getTextualRepresentation().equals("1")) {
                    return "RowVector";
                } else if (mathValueType.getDimensions().get(1).getTextualRepresentation().equals("1")) {
                    return "ColumnVector";
                }
                return "Matrix";//TODO improve in future
            } else {
                Log.error("0xGEEXCOMAVAT Type conversion Case not handled!");
            }
        } else {
            Log.error("Case not handled!");
        }
        return result;
    }

    public static String generateExecuteCode(MathNameExpressionSymbol mathNameExpressionSymbol, List<String> includeStrings) {
        Log.info(mathNameExpressionSymbol.getNameToResolveValue(), "NameToResolveValue:");
        return mathNameExpressionSymbol.getNameToResolveValue();
    }


    public static String generateExecuteCode(MathNumberExpressionSymbol mathNumberExpressionSymbol, List<String> includeStrings) {
        return mathNumberExpressionSymbol.getValue().toString();
    }

    public static String generateExecuteCode(MathAssignmentExpressionSymbol mathAssignmentExpressionSymbol, List<String> includeStrings) {
        Log.info(mathAssignmentExpressionSymbol.getTextualRepresentation(), "mathAssignmentExpressionSymbol:");

        if (mathAssignmentExpressionSymbol.getMathMatrixAccessOperatorSymbol() != null) {
            if (MathFunctionFixer.fixForLoopAccess(mathAssignmentExpressionSymbol.getNameOfMathValue(), ComponentConverter.currentBluePrint)) {

                String result = mathAssignmentExpressionSymbol.getNameOfMathValue();
                result += ExecuteMethodGeneratorMatrixExpressionHandler.generateExecuteCode(mathAssignmentExpressionSymbol.getMathMatrixAccessOperatorSymbol(), includeStrings, true) + " ";
                result += mathAssignmentExpressionSymbol.getAssignmentOperator().getOperator() + " ";
                result += ExecuteMethodGenerator.generateExecuteCode(mathAssignmentExpressionSymbol.getExpressionSymbol(), includeStrings) + ";\n";

                return result;

            }
            /*if (mathAssignmentExpressionSymbol.getNameOfMathValue().equals("eigenVectors")) {
                for (Variable var : ComponentConverter.currentBluePrint.getMathInformationRegister().getVariables()) {
                    Log.info(var.getName(), "Var:");
                }
            }*/
            String result = mathAssignmentExpressionSymbol.getNameOfMathValue();
            result += ExecuteMethodGeneratorMatrixExpressionHandler.generateExecuteCode(mathAssignmentExpressionSymbol.getMathMatrixAccessOperatorSymbol(), includeStrings) + " ";
            result += mathAssignmentExpressionSymbol.getAssignmentOperator().getOperator() + " ";
            result += ExecuteMethodGenerator.generateExecuteCode(mathAssignmentExpressionSymbol.getExpressionSymbol(), includeStrings) + ";\n";

            return result;
        }

        return mathAssignmentExpressionSymbol.getNameOfMathValue() + " " + mathAssignmentExpressionSymbol.getAssignmentOperator().getOperator() + " " + ExecuteMethodGenerator.generateExecuteCode(mathAssignmentExpressionSymbol.getExpressionSymbol(), includeStrings) + ";\n";
    }


    public static String generateExecuteCode(MathForLoopExpressionSymbol mathForLoopExpressionSymbol, List<String> includeStrings) {
        String result = "";
        //For loop head
        result += generateExecuteCode(mathForLoopExpressionSymbol.getForLoopHead(), includeStrings);
        //For loop body
        result += "{\n";
        for (MathExpressionSymbol mathExpressionSymbol : mathForLoopExpressionSymbol.getForLoopBody())
            result += ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol, includeStrings);
        result += "}\n";

        return result;
    }

    public static String generateExecuteCode(MathForLoopHeadSymbol mathForLoopHeadSymbol, List<String> includeStrings) {
        String result = "";
        result += ForLoopHeadConverter.getForLoopHeadCode(mathForLoopHeadSymbol, includeStrings);
        return result;
    }

    public static String generateExecuteCode(MathArithmeticExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        String result = "";
        if (mathExpressionSymbol.getMathOperator().equals("^")) {
            List<MathExpressionSymbol> list = new ArrayList<MathExpressionSymbol>();
            list.add(mathExpressionSymbol.getLeftExpression());
            list.add(mathExpressionSymbol.getRightExpression());
            String valueListString = "(" + OctaveHelper.getOctaveValueListString(list) + ")";
            return OctaveHelper.getCallBuiltInFunctionFirstResult(mathExpressionSymbol.getLeftExpression(), "Fmpower", valueListString, false, 1);
        } else {
            result += /*"("+*/  ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol.getLeftExpression(), includeStrings) + mathExpressionSymbol.getMathOperator();

            if (mathExpressionSymbol.getRightExpression() != null)
                result += ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol.getRightExpression(), includeStrings);
        }
        return result;

    }


    public static String generateExecuteCode(MathConditionalExpressionsSymbol mathConditionalExpressionsSymbol, List<String> includeStrings) {
        String result = "";

        //if condition
        result += ExecuteMethodGeneratorHelper.generateIfConditionCode(mathConditionalExpressionsSymbol.getIfConditionalExpression(), includeStrings);
        //else if condition
        for (MathConditionalExpressionSymbol mathConditionalExpressionSymbol : mathConditionalExpressionsSymbol.getIfElseConditionalExpressions())
            result += "else " + ExecuteMethodGeneratorHelper.generateIfConditionCode(mathConditionalExpressionSymbol, includeStrings);
        //else block
        if (mathConditionalExpressionsSymbol.getElseConditionalExpression().isPresent()) {
            result += "else " + ExecuteMethodGeneratorHelper.generateIfConditionCode(mathConditionalExpressionsSymbol.getElseConditionalExpression().get(), includeStrings);
        }
        return result;
    }

}