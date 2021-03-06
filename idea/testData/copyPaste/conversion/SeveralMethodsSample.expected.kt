class A {
    fun someOther() = false

    private fun formatElement(var element : PsiElement) : String {
        element = JetPsiUtil.ascendIfPropertyAccessor(element)
        if (element is JetNamedFunction || element is JetProperty)
        {
            val bindingContext = AnalyzerFacadeWithCache.analyzeFileWithCache((element.getContainingFile() as JetFile)).getBindingContext()

            val declarationDescriptor = bindingContext.get(BindingContext.DECLARATION_TO_DESCRIPTOR, element)
            if (declarationDescriptor is CallableMemberDescriptor)
            {
                val containingDescriptor = declarationDescriptor.getContainingDeclaration()
                if (containingDescriptor is ClassDescriptor)
                {
                    return JetBundle.message("x.in.y", DescriptorRenderer.COMPACT.render(declarationDescriptor), DescriptorRenderer.SOURCE_CODE_SHORT_NAMES_IN_TYPES.render(containingDescriptor))
                }
            }
        }

        assert(element is PsiMethod) {"Method accepts only kotlin functions/properties and java methods, but '" + element.getText() + "' was found"}
        return JetRefactoringUtil.formatPsiMethod((element as PsiMethod), true, false)
    }

    protected fun getDimensionServiceKey() : String {
        return "#org.jetbrains.jet.plugin.refactoring.safeDelete.KotlinOverridingDialog"
    }

    public fun getSelected() : ArrayList<UsageInfo> {
        val result = ArrayList<UsageInfo>()
        for (i in 0..myChecked.length - 1) {
            if (myChecked[i])
            {
                result.add(myOverridingMethods.get(i))
            }
        }
        return result
    }
}