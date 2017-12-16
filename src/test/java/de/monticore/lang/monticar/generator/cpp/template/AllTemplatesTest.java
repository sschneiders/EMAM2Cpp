package de.monticore.lang.monticar.generator.cpp.template;

import de.monticore.lang.monticar.generator.cpp.viewmodel.ComponentStreamTestViewModel;
import de.monticore.lang.monticar.generator.cpp.viewmodel.StreamViewModel;
import de.monticore.lang.monticar.generator.cpp.viewmodel.TestsMainEntryViewModel;
import de.monticore.lang.monticar.generator.cpp.viewmodel.check.BooleanOutputPortCheck;
import de.monticore.lang.monticar.generator.cpp.viewmodel.check.ComponentCheckViewModel;
import de.monticore.lang.monticar.generator.cpp.viewmodel.check.RangeOutputPortCheck;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AllTemplatesTest {

    @Test
    public void testGenerateMainEntry() {
        TestsMainEntryViewModel vm = new TestsMainEntryViewModel();
        vm.setIncludes(
                Arrays.asList("cmp1.h", "cmp2.h", "cmp3.h")
        );
        String result = AllTemplates.generateMainEntry(vm);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("#include \"cmp1.h\""));
        Assert.assertTrue(result.contains("#include \"cmp2.h\""));
        Assert.assertTrue(result.contains("#include \"cmp3.h\""));
    }

    @Test
    public void testGenerateComponentStreamTest() {
        ComponentStreamTestViewModel vm = new ComponentStreamTestViewModel();
        vm.setComponentName("my_cmp1");
        vm.setFileNameWithoutExtension("my_cmp1_test");
        vm.setStreams(new ArrayList<>());

        StreamViewModel stream = new StreamViewModel();
        vm.getStreams().add(stream);
        stream.setName("Test1.stream");
        stream.setChecks(new ArrayList<>());
        ComponentCheckViewModel check = new ComponentCheckViewModel();
        stream.getChecks().add(check);
        check.setInputPortName2Value(new HashMap<>());
        check.getInputPortName2Value().put("in1", "12.3");
        check.getInputPortName2Value().put("in2", "true");
        check.getInputPortName2Value().put("in3", "7");
        check.setOutputPortName2Check(new HashMap<>());
        check.getOutputPortName2Check().put("out1", BooleanOutputPortCheck.TRUE_EXPECTED);
        check.getOutputPortName2Check().put("out3", RangeOutputPortCheck.from(1.5, 2.5));
        check.getOutputPortName2Check().put("out4", RangeOutputPortCheck.from(1, 2));
        check.getOutputPortName2Check().put("out5", RangeOutputPortCheck.from(1, 1));
        ComponentCheckViewModel s1c2 = new ComponentCheckViewModel();
        stream.getChecks().add(s1c2);
        s1c2.setInputPortName2Value(new HashMap<>());
        s1c2.getInputPortName2Value().put("in1", "0.11");
        s1c2.getInputPortName2Value().put("in2", "false");
        s1c2.getInputPortName2Value().put("in3", "15");
        s1c2.setOutputPortName2Check(new HashMap<>());
        s1c2.getOutputPortName2Check().put("out1", BooleanOutputPortCheck.FALSE_EXPECTED);
        s1c2.getOutputPortName2Check().put("out2", BooleanOutputPortCheck.TRUE_EXPECTED);
        s1c2.getOutputPortName2Check().put("out4", RangeOutputPortCheck.from(1, 2));
        s1c2.getOutputPortName2Check().put("out5", RangeOutputPortCheck.from(0, 0));

        String result = AllTemplates.generateComponentStreamTest(vm);
        Assert.assertNotNull(result);
        String[] expectedFragments = new String[]{
                "#ifndef MY_CMP1_TEST",
                "#define MY_CMP1_TEST",
                "#include \"catch.hpp\"",
                "#include \"../my_cmp1.h\"",
                "TEST_CASE(\"Test1.stream\", \"[my_cmp1]\")",
                "my_cmp1 component;",
                "component.init();",
                "component.in2 = true;",
                "component.in1 = 12.3;",
                "component.in3 = 7;",
                "component.execute();",
                "REQUIRE( component.out3 >= 1.5 );",
                "REQUIRE( component.out3 <= 2.5 );",
                "REQUIRE( component.out4 >= 1.0 );",
                "REQUIRE( component.out4 <= 2.0 );",
                "REQUIRE( component.out5 >= 1.0 );",
                "REQUIRE( component.out5 <= 1.0 );",
                "REQUIRE( component.out1 );",
                "component.in2 = false;",
                "component.in1 = 0.11;",
                "component.in3 = 15;",
                "component.execute();",
                "REQUIRE( component.out2 );",
                "REQUIRE( component.out4 >= 1.0 );",
                "REQUIRE( component.out4 <= 2.0 );",
                "REQUIRE( component.out5 >= 0.0 );",
                "REQUIRE( component.out5 <= 0.0 );",
                "REQUIRE_FALSE( component.out1 );",
                "#endif",
        };
        for (String f : expectedFragments) {
            Assert.assertTrue("fragment " + f + " was expected", result.contains(f));
        }
    }
}
