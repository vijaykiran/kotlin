/*
 * Copyright 2010-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.evaluate;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.regex.Pattern;
import org.jetbrains.jet.JetTestUtils;
import org.jetbrains.jet.test.InnerTestClasses;
import org.jetbrains.jet.test.TestMetadata;

import org.jetbrains.jet.evaluate.AbstractEvaluateExpressionTest;

/** This class is generated by {@link org.jetbrains.jet.generators.tests.GenerateTests}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@InnerTestClasses({EvaluateExpressionTestGenerated.Constant.class, EvaluateExpressionTestGenerated.IsPure.class})
public class EvaluateExpressionTestGenerated extends AbstractEvaluateExpressionTest {
    @TestMetadata("compiler/testData/evaluate/constant")
    public static class Constant extends AbstractEvaluateExpressionTest {
        public void testAllFilesPresentInConstant() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.GenerateTests", new File("compiler/testData/evaluate/constant"), Pattern.compile("^(.+)\\.kt$"), true);
        }
        
        @TestMetadata("compareTo.kt")
        public void testCompareTo() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/compareTo.kt");
        }
        
        @TestMetadata("divideByZero.kt")
        public void testDivideByZero() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/divideByZero.kt");
        }
        
        @TestMetadata("equals.kt")
        public void testEquals() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/equals.kt");
        }
        
        @TestMetadata("float.kt")
        public void testFloat() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/float.kt");
        }
        
        @TestMetadata("floatsAndDoubles.kt")
        public void testFloatsAndDoubles() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/floatsAndDoubles.kt");
        }
        
        @TestMetadata("integers.kt")
        public void testIntegers() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/integers.kt");
        }
        
        @TestMetadata("strings.kt")
        public void testStrings() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/strings.kt");
        }
        
        @TestMetadata("unaryMinusIndepWoExpType.kt")
        public void testUnaryMinusIndepWoExpType() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/unaryMinusIndepWoExpType.kt");
        }
        
        @TestMetadata("unaryMinusIndependentExpType.kt")
        public void testUnaryMinusIndependentExpType() throws Exception {
            doConstantTest("compiler/testData/evaluate/constant/unaryMinusIndependentExpType.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/evaluate/isPure")
    public static class IsPure extends AbstractEvaluateExpressionTest {
        public void testAllFilesPresentInIsPure() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.GenerateTests", new File("compiler/testData/evaluate/isPure"), Pattern.compile("^(.+)\\.kt$"), true);
        }
        
        @TestMetadata("enum.kt")
        public void testEnum() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/enum.kt");
        }
        
        @TestMetadata("innerToType.kt")
        public void testInnerToType() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/innerToType.kt");
        }
        
        @TestMetadata("strings.kt")
        public void testStrings() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/strings.kt");
        }
        
        @TestMetadata("toType.kt")
        public void testToType() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/toType.kt");
        }
        
        @TestMetadata("unaryMinusIndepWoExpType.kt")
        public void testUnaryMinusIndepWoExpType() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/unaryMinusIndepWoExpType.kt");
        }
        
        @TestMetadata("unaryMinusIndependentExpType.kt")
        public void testUnaryMinusIndependentExpType() throws Exception {
            doIsPureTest("compiler/testData/evaluate/isPure/unaryMinusIndependentExpType.kt");
        }
        
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("EvaluateExpressionTestGenerated");
        suite.addTestSuite(Constant.class);
        suite.addTestSuite(IsPure.class);
        return suite;
    }
}