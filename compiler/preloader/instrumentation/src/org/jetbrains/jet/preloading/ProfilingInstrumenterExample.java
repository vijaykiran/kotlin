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

package org.jetbrains.jet.preloading;

import org.jetbrains.jet.preloading.instrumentation.InterceptionInstrumenterAdaptor;
import org.jetbrains.jet.preloading.instrumentation.annotations.MethodInterceptor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class ProfilingInstrumenterExample extends InterceptionInstrumenterAdaptor {

    // How many times are visit* methods of visitors called?
    @MethodInterceptor(className = ".*Visitor.*", methodName = "visit.*", methodDesc = ".*", allowMultipleMatches = true)
    public static final Object a = new InvocationCount();

    public static class InvocationCount {
        private int count = 0;

        public void enter() {
            // This method is called upon entering a visit* method
            count++;
        }

        public void dump(PrintStream out) {
            // This method is called upon program termination
            out.println("Invocation count: " + count);
        }
    }

    // How much time do we spend in equals() methods of all classes inside package org
    // NOTE: this works only on methods actually DECLARED in these classes
    // This also logs names of actually instrumented methods to console
    @MethodInterceptor(className = "org/.*", methodName = "equals", methodDesc = "\\(Ljava/lang/Object;\\)Z", logInterceptions = true)
    public static final Object b = new TotalTime();

    public static class TotalTime {
        private long time = 0;
        private long start = 0;
        private boolean started = false;

        public void enter() {
            if (!started) {
                start = System.nanoTime();
                started = true;
            }
        }

        public void exit() {
            if (started) {
                time += System.nanoTime() - start;
                started = false;
            }
        }

        public void dump(PrintStream out) {
            out.printf("Total time: %.3fs\n", (time / 1e9));
        }
    }

    // Collect all strings that were capitalized using StringUtil
    @MethodInterceptor(className = "com/intellij/openapi/util/text/StringUtil",
                       methodName = "capitalize",
                       methodDesc = "\\(Ljava/lang/String;\\).*")
    public static Object c = new CollectFirstArguments();

    public static class CollectFirstArguments {

        private final List<Object> arguments = new ArrayList<Object>(30000);

        public void enter(Object arg) {
            arguments.add(arg);
        }

        public void dump(PrintStream out) {
            System.out.println("Different values: " + new HashSet<Object>(arguments).size());
        }
    }
}