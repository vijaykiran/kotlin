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

package org.jetbrains.jet.j2k

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.JavaRecursiveElementVisitor
import java.util.LinkedHashSet

fun isConstructorPrimary(constructor: PsiMethod): Boolean {
    val parent = constructor.getParent()
    if (parent is PsiClass) {
        if (parent.getConstructors().size == 1) {
            return true
        }
        else {
            val c = getPrimaryConstructorForThisCase(parent)
            if (c != null && c.hashCode() == constructor.hashCode()) {
                return true
            }
        }
    }
    return false
}

private fun getPrimaryConstructorForThisCase(psiClass: PsiClass): PsiMethod? {
    val tv = FindPrimaryConstructorVisitor()
    psiClass.accept(tv)
    return tv.getPrimaryConstructor()
}

private class FindPrimaryConstructorVisitor() : JavaRecursiveElementVisitor() {
    private val myResolvedConstructors = LinkedHashSet<PsiMethod>()

    override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
        for (r in expression?.getReferences()!!) {
            if (r.getCanonicalText() == "this") {
                val res: PsiElement? = r.resolve()
                if (res is PsiMethod && res.isConstructor()) {
                    myResolvedConstructors.add(res)
                }
            }
        }
    }

    fun getPrimaryConstructor(): PsiMethod? {
        if (myResolvedConstructors.size() > 0) {
            val first: PsiMethod = myResolvedConstructors.iterator().next()
            for (m in myResolvedConstructors)
                if (m.hashCode() != first.hashCode()) {
                    return null
                }

            return first
        }
        return null
    }
}

