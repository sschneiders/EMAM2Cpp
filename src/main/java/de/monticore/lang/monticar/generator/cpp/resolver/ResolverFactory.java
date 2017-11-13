package de.monticore.lang.monticar.generator.cpp.resolver;

import java.nio.file.Path;

public class ResolverFactory {

    private Path[] modelPaths;

    public ResolverFactory(Path... modelPaths) {
        this.modelPaths = modelPaths;
    }

    public Resolver get() {
        SymTabCreator symTabCreator = new SymTabCreator(modelPaths);
        return new Resolver(symTabCreator.createSymTab());
    }
}
