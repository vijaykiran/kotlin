package org.jetbrains.jet.lang.resolve.java.lazy.descriptors

import org.jetbrains.jet.lang.descriptors.*
import org.jetbrains.jet.lang.resolve.DescriptorUtils
import org.jetbrains.jet.lang.resolve.name.Name
import java.util.Collections
import org.jetbrains.jet.lang.resolve.java.lazy.LazyJavaResolverContext
import org.jetbrains.jet.lang.resolve.java.lazy.withTypes
import org.jetbrains.jet.lang.resolve.java.lazy.TypeParameterResolver
import org.jetbrains.jet.lang.resolve.java.structure.JavaPackage
import org.jetbrains.jet.lang.resolve.name.FqName
import org.jetbrains.jet.utils.flatten
import org.jetbrains.jet.lang.resolve.java.structure.JavaClass

public abstract class LazyJavaPackageFragmentScope(
        c: LazyJavaResolverContext,
        packageFragment: LazyJavaPackageFragment
) : LazyJavaMemberScope(c.withTypes(), packageFragment) {
    
    protected val fqName: FqName = DescriptorUtils.getFQName(packageFragment).toSafe()
    private val classes = c.storageManager.createMemoizedFunctionWithNullableValues<Name, ClassDescriptor> {
        name ->
        val fqName = fqName.child(name)
        val javaClass = c.finder.findClass(fqName)
        if (javaClass == null)
            null
        else
            LazyJavaClassDescriptor(c.withTypes(TypeParameterResolver.EMPTY), packageFragment, fqName, javaClass)
    }

    override fun addExtraDescriptors(result: MutableCollection<in DeclarationDescriptor>) {
        // no extra descriptors
    }

    override fun getClassifier(name: Name): ClassifierDescriptor? = classes(name)

    override fun getNamespace(name: Name): NamespaceDescriptor? = c.subModule.getPackageFragment(getContainingDeclaration().getFqName().child(name))

    override fun getImplicitReceiversHierarchy(): List<ReceiverParameterDescriptor> = listOf()

    override fun getContainingDeclaration()= super.getContainingDeclaration() as LazyJavaPackageFragment
}

public class LazyPackageFragmentScopeForJavaPackage(
        c: LazyJavaResolverContext,
        private val jPackage: JavaPackage,
        packageFragment: LazyJavaPackageFragment
) : LazyJavaPackageFragmentScope(c, packageFragment) {

    override fun getAllClassNames(): Collection<Name> {
        val javaPackage = c.finder.findPackage(fqName)
        assert(javaPackage != null) { "Package not found:  $fqName" }
        return javaPackage!!.getClasses().map { c -> c.getName() }
    }

    override fun getAllPackageNames(): Collection<Name> =
            listOf(
                jPackage.getClasses().map { c -> c.getName() },
                jPackage.getSubPackages().map { sp -> sp.getFqName().shortName() }
            ).flatten()


    override fun getProperties(name: Name): Collection<VariableDescriptor> = Collections.emptyList()
    override fun getAllPropertyNames() = Collections.emptyList<Name>()

    override fun getFunctions(name: Name): Collection<FunctionDescriptor> = Collections.emptyList()
    override fun getAllFunctionNames() = Collections.emptyList<Name>()
}

public class LazyPackageFragmentScopeForJavaClass(
        c: LazyJavaResolverContext,
        private val jClass: JavaClass,
        packageFragment: LazyJavaPackageFragment
) : LazyJavaPackageFragmentScope(c, packageFragment) {

    override fun getAllClassNames(): Collection<Name> = jClass.getInnerClasses().map { c -> c.getName() }
    override fun getAllPackageNames(): Collection<Name> = jClass.getInnerClasses().filter { c -> c.isStatic() }.map { c -> c.getName() }

    // TODO
    override fun getProperties(name: Name): Collection<VariableDescriptor> = Collections.emptyList()
    override fun getAllPropertyNames() = Collections.emptyList<Name>()

    // TODO
    override fun getFunctions(name: Name): Collection<FunctionDescriptor> = Collections.emptyList()
    override fun getAllFunctionNames() = Collections.emptyList<Name>()
}
