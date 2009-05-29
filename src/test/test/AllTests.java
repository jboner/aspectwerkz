/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import test.expression.ExpressionParserTest;
import test.expression.AnnotationExpressionTest;
import test.expression.AdvisedClassFilterExpressionTest;
import test.enclosingsjp.EnclosingSJPTest;
import test.staticinitialization.StaticInitializationTest;
import test.staticinitialization.StaticInitializationExpressionTest;
import test.withincode.WithincodeClinitTest;
import test.customproceed.aw438.ArrayInCustomProceedTest;
import test.withinstaticref.WithinStaticRefTest;
import test.reflection.ClassInfoHelperTest;
import test.fieldsetbug.FieldSetTest;
import test.perx.AssociationScopeTest;

/**
 * This tests can be run without online / offline or other post compilation mode. Used to test
 * standalone component of AspectWerkz.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bonér </a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class AllTests extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("All tests");

        suite.addTestSuite(MemberMethodAdviceTest.class);
        suite.addTestSuite(StaticMethodAdviceTest.class);
        suite.addTestSuite(CallerSideAdviceTest.class);
        suite.addTestSuite(FieldAdviceTest.class);
        suite.addTestSuite(ClassPatternTest.class);
        suite.addTestSuite(ExceptionTest.class);
        suite.addTestSuite(MethodComparatorTest.class);
        suite.addTestSuite(StringsTest.class);
        suite.addTestSuite(test.deployment.DeployerTest.class);
        suite.addTestSuite(test.callAndExecution.CallExecutionTest.class);
        suite.addTestSuite(test.hierarchicalpattern.HierachicalPatternTest.class);
        suite.addTestSuite(test.abstractclass.AbstractClassTest.class);
        suite.addTestSuite(test.reflection.ReflectionTest.class);
        suite.addTestSuite(test.pointcutexpression.PointcutExpressionTest.class);
        suite.addTestSuite(test.args.ArgsAdviceTest.class);
        suite.addTestSuite(test.afterxxx.Test.class);
        suite.addTestSuite(test.expression.ExpressionTest.class);
        suite.addTestSuite(test.rtti.RttiTest.class);
        suite.addTestSuite(test.proceedinnewthread.ProceedTest.class);
        suite.addTestSuite(test.constructor.ConstructorAdviceTest.class);
        suite.addTestSuite(test.bindingsyntax.AdviceBindingTest.class);
        suite.addTestSuite(test.orthogonal.OrthogonalTest.class);
        suite.addTestSuite(test.annotation.AnnotationTest.class);
        suite.addTestSuite(test.modifier.ModifierTest.class);
        suite.addTestSuite(test.superclassconstructor.SuperClassConstructorTest.class);
        suite.addTestSuite(test.staticfield.StaticFieldAdviceTest.class);
        suite.addTestSuite(test.SerialVerUidTest.class);
        suite.addTestSuite(test.thistarget.TargetTest.class);
        suite.addTestSuite(test.thistarget.TargetReferencedAndRuntimeCheckTest.class);
        suite.addTestSuite(test.optimizations.OptimizeTest.class);
        suite.addTestSuite(test.annotation.AnnotationParserTest.class);
        suite.addTestSuite(test.annotation.AnnotationCTest.class);
        suite.addTestSuite(test.polymorphic.PolymorphicTest.class);
        suite.addTestSuite(test.annotation.AnnotationCopyTest.class);
        suite.addTestSuite(test.mixin.perinstance.IntroductionTest.class);
        suite.addTestSuite(test.mixin.perclass.IntroductionTest.class);
        suite.addTestSuite(test.mixin.perjvm.IntroductionTest.class);
        suite.addTestSuite(test.adviseonintroducedinterface.Test.class);
        suite.addTestSuite(test.advisetostringbug.Test.class);//mixin
        suite.addTestSuite(test.implementsbug.ImplementsTest.class);
        suite.addTestSuite(test.inheritedmixinbug.Target.class);
        suite.addTestSuite(test.intercept.execution.InterceptTest.class);
        suite.addTestSuite(test.intercept.call.InterceptTest.class);
        suite.addTestSuite(test.intercept.set.InterceptTest.class);
        suite.addTestSuite(test.intercept.get.InterceptTest.class);
        suite.addTestSuite(test.intercept.handler.InterceptTest.class);
        suite.addTestSuite(test.handler.HandlerTest.class);
        suite.addTestSuite(test.customproceed.CustomProceedTest.class);
        suite.addTestSuite(CFlowTest.class);
        suite.addTestSuite(ComplexCFlowTest.class);
        suite.addTestSuite(CflowCompilerTest.class);
        suite.addTestSuite(ExpressionParserTest.class);
        suite.addTestSuite(AnnotationExpressionTest.class);
        suite.addTestSuite(AdvisedClassFilterExpressionTest.class);
        suite.addTestSuite(EnclosingSJPTest.class);
        suite.addTestSuite(StaticInitializationTest.class);
        suite.addTestSuite(StaticInitializationExpressionTest.class);
        suite.addTestSuite(WithincodeClinitTest.class);
        suite.addTestSuite(FieldSetTest.class);
        suite.addTestSuite(ArrayInCustomProceedTest.class);
        suite.addTestSuite(WithinStaticRefTest.class);
        suite.addTestSuite(ClassInfoTest.class);
        suite.addTestSuite(ClassInfoHelperTest.class);
        suite.addTestSuite(AssociationScopeTest.class);

        // AW-361, should not throw an NPE
        SystemDefinitionContainer.getDefinitionsFor(null);
        SystemDefinitionContainer.getDefinitionsAt(null);
        SystemDefinitionContainer.getVirtualDefinitionAt(null);

        // NOT YET COMPLETED
//        suite.addTestSuite(test.aopc.AspectSystemTest.class);//FIXME - test broken since Attr.Extractor removal
//        suite.addTestSuite(test.mixindeployment.IntroductionDeploymentTest.class);
//        suite.addTestSuite(test.aspectutilmethodbug.Test.class);//is using perThread
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}