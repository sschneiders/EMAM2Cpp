package de.monticore.lang.monticar.generator.cpp.commands;

import de.monticore.lang.math.math._symboltable.expression.MathExpressionSymbol;
import de.monticore.lang.math.math._symboltable.matrix.MathMatrixAccessSymbol;
import de.monticore.lang.math.math._symboltable.matrix.MathMatrixNameExpressionSymbol;
import de.monticore.lang.monticar.generator.BluePrint;
import de.monticore.lang.monticar.generator.MathCommand;
import de.monticore.lang.monticar.generator.cpp.BluePrintCPP;
import de.monticore.lang.monticar.generator.cpp.OctaveHelper;
import de.monticore.lang.monticar.generator.cpp.converter.ComponentConverter;
import de.monticore.lang.monticar.generator.cpp.converter.ExecuteMethodGenerator;
import de.monticore.lang.monticar.generator.cpp.converter.MathConverter;
import de.monticore.lang.monticar.generator.cpp.symbols.MathStringExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Not working on Windows 10 Octave version 4.2.1. Tested on two different Windows 10 machines.
 * Issue seems to be that Windows 10 is not yet fully supported by Octave.
 *
 * @author Sascha Schneiders
 */
public class MathKMeansIDXCommand extends MathCommand {
    public MathKMeansIDXCommand() {
        setMathCommandName("kmeansidx");
        //setTargetCommand("Helper::callOctaveFunctionDouble");
    }

    @Override
    public void convert(MathExpressionSymbol mathExpressionSymbol, BluePrint bluePrint) {
        MathMatrixNameExpressionSymbol mathMatrixNameExpressionSymbol = (MathMatrixNameExpressionSymbol) mathExpressionSymbol;

        mathMatrixNameExpressionSymbol.setNameToAccess("");

        String valueListString = "";
        for (MathMatrixAccessSymbol accessSymbol : mathMatrixNameExpressionSymbol.getMathMatrixAccessOperatorSymbol().getMathMatrixAccessSymbols())
            MathConverter.fixMathFunctions(accessSymbol, (BluePrintCPP) bluePrint);
        valueListString += ExecuteMethodGenerator.generateExecuteCode(mathExpressionSymbol, new ArrayList<String>());
        //OctaveHelper.getCallOctaveFunction(mathExpressionSymbol, "sum","Double", valueListString));
        List<MathMatrixAccessSymbol> newMatrixAccessSymbols = new ArrayList<>();
        MathStringExpression stringExpression = new MathStringExpression(OctaveHelper.getCallOctaveFunction(mathExpressionSymbol, "kmeans", "ColumnVector", valueListString, "FirstResult", false));
        newMatrixAccessSymbols.add(new MathMatrixAccessSymbol(stringExpression));

        mathMatrixNameExpressionSymbol.getMathMatrixAccessOperatorSymbol().setMathMatrixAccessSymbols(newMatrixAccessSymbols);
        ((BluePrintCPP) bluePrint).addAdditionalIncludeString("octave/builtin-defun-decls");
    }
}
