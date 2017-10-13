package de.monticore.lang.monticar.generator.cpp.converter;

import de.monticore.lang.math.math._symboltable.expression.MathExpressionSymbol;
import de.monticore.lang.math.math._symboltable.matrix.*;
import de.monticore.lang.monticar.generator.cpp.MathFunctionFixer;
import de.monticore.lang.monticar.generator.cpp.OctaveHelper;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sascha Schneiders
 */
public class ExecuteMethodGeneratorMatrixExpressionHandler {


    public static String generateExecuteCode(MathMatrixArithmeticExpressionSymbol mathMatrixArithmeticExpressionSymbol, List<String> includeStrings) {
        String result = "";
        if (mathMatrixArithmeticExpressionSymbol.getMathOperator().equals("^")) {
            return generateExecuteCodeMatrixPowerOfOperator(mathMatrixArithmeticExpressionSymbol, includeStrings);
        } else if (mathMatrixArithmeticExpressionSymbol.getMathOperator().equals(".^")) {
            return generateExecuteCodeMatrixEEPowerOf(mathMatrixArithmeticExpressionSymbol, includeStrings);
        } else if (mathMatrixArithmeticExpressionSymbol.getMathOperator().equals("./")) {
            return generateExecuteCodeMatrixEEDivide(mathMatrixArithmeticExpressionSymbol, includeStrings);
        /*} else if (mathArithmeticExpressionSymbol.getMathOperator().equals("./")) {
            Log.error("reace");
            result += "\"ldivide\"";
            includeStrings.add("Helper");
        */
        } else {

            result += /*"(" +*/ ExecuteMethodGenerator.generateExecuteCode(mathMatrixArithmeticExpressionSymbol.getLeftExpression(), includeStrings) + " " + mathMatrixArithmeticExpressionSymbol.getMathOperator();

            if (mathMatrixArithmeticExpressionSymbol.getRightExpression() != null)
                result += " " + ExecuteMethodGenerator.generateExecuteCode(mathMatrixArithmeticExpressionSymbol.getRightExpression(), includeStrings);
        }
        /*result += ")"*/
        ;

        return result;
    }

    public static String generateExecuteCodeMatrixEEDivide(MathMatrixArithmeticExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        List<MathExpressionSymbol> list = new ArrayList<MathExpressionSymbol>();
        list.add(mathExpressionSymbol.getLeftExpression());
        list.add(mathExpressionSymbol.getRightExpression());
        String valueListString = "(" + OctaveHelper.getOctaveValueListString(list) + ")";
        return OctaveHelper.getCallOctaveFunctionFirstResult(mathExpressionSymbol.getLeftExpression(), "ldivide", valueListString, false);
    }

    public static String generateExecuteCodeMatrixEEPowerOf(MathMatrixArithmeticExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        List<MathExpressionSymbol> list = new ArrayList<MathExpressionSymbol>();
        list.add(mathExpressionSymbol.getLeftExpression());
        list.add(mathExpressionSymbol.getRightExpression());
        String valueListString = "(" + OctaveHelper.getOctaveValueListString(list) + ")";
        return OctaveHelper.getCallOctaveFunctionFirstResult(mathExpressionSymbol.getLeftExpression(), "power", valueListString, false);
    }

    public static String generateExecuteCodeMatrixPowerOfOperator(MathMatrixArithmeticExpressionSymbol mathExpressionSymbol, List<String> includeStrings) {
        List<MathExpressionSymbol> list = new ArrayList<MathExpressionSymbol>();
        list.add(mathExpressionSymbol.getLeftExpression());
        list.add(mathExpressionSymbol.getRightExpression());
        String valueListString = "(" + OctaveHelper.getOctaveValueListString(list) + ")";
        return OctaveHelper.getCallBuiltInFunctionFirstResult(mathExpressionSymbol.getLeftExpression(), "Fmpower", valueListString, false, 1);
    }

    public static String generateExecuteCode(MathMatrixAccessOperatorSymbol mathMatrixAccessOperatorSymbol, List<String> includeStrings, boolean setMinusOne) {
        String result = "";
        int counter = 0;
        int ignoreCounterAt = -1;
        int counterSetMinusOne = -1;
        if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().size() == 2) {
            if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().get(0).isDoubleDot()) {
                ignoreCounterAt = 0;
                result += ".column";
                counterSetMinusOne = 1;
            } else if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().get(1).isDoubleDot()) {
                ignoreCounterAt = 1;
                result += ".row";
                counterSetMinusOne = 0;
            }
        }
        result += mathMatrixAccessOperatorSymbol.getAccessStartSymbol();


        for (MathMatrixAccessSymbol mathMatrixAccessSymbol : mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols()) {
            if (counter == ignoreCounterAt) {
                ++counter;
            } else {
                if (counter == counterSetMinusOne) {
                    result += generateExecuteCode(mathMatrixAccessSymbol, includeStrings, true);
                } else
                    result += generateExecuteCode(mathMatrixAccessSymbol, includeStrings, setMinusOne);
                ++counter;
                if (counter < mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().size() && counter != ignoreCounterAt) {
                    result += ", ";
                }
            }
        }


        result += mathMatrixAccessOperatorSymbol.getAccessEndSymbol();
        return result;
    }

    public static String generateExecuteCode(MathMatrixAccessOperatorSymbol mathMatrixAccessOperatorSymbol, List<String> includeStrings) {
        String result = "";
        int counter = 0;
        int ignoreCounterAt = -1;
        int counterSetMinusOne = -1;
        if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().size() == 2) {
            if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().get(0).isDoubleDot()) {
                ignoreCounterAt = 0;
                result += ".column";
                counterSetMinusOne = 1;
            } else if (mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().get(1).isDoubleDot()) {
                ignoreCounterAt = 1;
                result += ".row";
                counterSetMinusOne = 0;
            }
        }
        result += mathMatrixAccessOperatorSymbol.getAccessStartSymbol();

        if (MathFunctionFixer.fixForLoopAccess(mathMatrixAccessOperatorSymbol.getMathMatrixNameExpressionSymbol(), ComponentConverter.currentBluePrint)) {
            for (MathMatrixAccessSymbol mathMatrixAccessSymbol : mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols()) {
                if (counter == ignoreCounterAt) {
                    ++counter;
                } else {
                    result += generateExecuteCode(mathMatrixAccessSymbol, includeStrings, true);
                    ++counter;
                    if (counter < mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().size() && counter != ignoreCounterAt) {
                        result += ", ";
                    }
                }
            }
        } else {
            for (MathMatrixAccessSymbol mathMatrixAccessSymbol : mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols()) {
                if (counter == ignoreCounterAt) {
                    ++counter;
                } else {
                    if (counter == counterSetMinusOne)
                        result += generateExecuteCode(mathMatrixAccessSymbol, includeStrings, true);
                    else
                        result += generateExecuteCode(mathMatrixAccessSymbol, includeStrings);
                    ++counter;
                    if (counter < mathMatrixAccessOperatorSymbol.getMathMatrixAccessSymbols().size() && counter != ignoreCounterAt) {
                        result += ", ";
                    }
                }
            }
        }
        result += mathMatrixAccessOperatorSymbol.getAccessEndSymbol();
        return result;
    }

    public static String generateExecuteCode(MathMatrixAccessSymbol mathMatrixAccessSymbol, List<String> includeStrings, boolean setMinusOne) {
        String result = "";

        if (mathMatrixAccessSymbol.isDoubleDot())
            result += ":";
        else
            result += ExecuteMethodGenerator.generateExecuteCode(mathMatrixAccessSymbol.getMathExpressionSymbol().get(), includeStrings);
        if (setMinusOne)
            result += "-1";
        return result;
    }

    public static String generateExecuteCode(MathMatrixAccessSymbol mathMatrixAccessSymbol, List<String> includeStrings) {
        return generateExecuteCode(mathMatrixAccessSymbol, includeStrings, false);
    }


    public static String generateExecuteCode(MathMatrixExpressionSymbol mathMatrixExpressionSymbol, List<String> includeStrings) {
        String result = "";


        if (mathMatrixExpressionSymbol.isMatrixArithmeticExpression()) {
            return generateExecuteCode((MathMatrixArithmeticExpressionSymbol) mathMatrixExpressionSymbol, includeStrings);
        } else if (mathMatrixExpressionSymbol.isMatrixAccessExpression()) {
            return generateExecuteCode((MathMatrixAccessSymbol) mathMatrixExpressionSymbol, includeStrings);
        } else if (mathMatrixExpressionSymbol.isMatrixNameExpression()) {
            return generateExecuteCode((MathMatrixNameExpressionSymbol) mathMatrixExpressionSymbol, includeStrings);
        } else if (mathMatrixExpressionSymbol.isValueExpression()) {
            return generateExecuteCode((MathMatrixArithmeticValueSymbol) mathMatrixExpressionSymbol, includeStrings);
        }
        Log.info(mathMatrixExpressionSymbol.getTextualRepresentation(), "Symbol:");
        Log.info(mathMatrixExpressionSymbol.getClass().getName(), "Symbol Name:");
        Log.error("0xMAMAEXSY Case not handled!");
        return result;
    }

    public static String generateExecuteCode(MathMatrixArithmeticValueSymbol mathMatrixArithmeticValueSymbol, List<String> includeStrings) {
        return MathConverter.getConstantConversion(mathMatrixArithmeticValueSymbol);
    }

    public static String generateExecuteCode(MathMatrixNameExpressionSymbol mathMatrixNameExpressionSymbol, List<String> includeStrings) {
        String result = "";
        //TODO fix matrix access parameter stuff
        result += mathMatrixNameExpressionSymbol.getNameToAccess();
        if (mathMatrixNameExpressionSymbol.hasMatrixAccessExpression()) {
            mathMatrixNameExpressionSymbol.getMathMatrixAccessOperatorSymbol().setMathMatrixNameExpressionSymbol(mathMatrixNameExpressionSymbol);
            result += generateExecuteCode(mathMatrixNameExpressionSymbol.getMathMatrixAccessOperatorSymbol(), includeStrings);
        }
        return result;
    }
}