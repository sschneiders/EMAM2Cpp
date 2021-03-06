package de.monticore.lang.monticar.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sascha Schneiders
 */
public class Method {
    String name;
    String typeName;
    List<Variable> parameters = new ArrayList<>();
    List<Instruction> instructions = new ArrayList<>();

    public Method() {

    }

    public Method(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public Method(String name, String typeName, List<Variable> parameters) {
        this.name = name;
        this.typeName = typeName;
        this.parameters = parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReturnTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void addParameter(Variable v) {
        parameters.add(v);
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public String getReturnTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }
}
