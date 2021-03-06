package generators

import java.io.*
import templates.*
import templates.Family.*

private val COMMON_AUTOGENERATED_WARNING: String = """//
// NOTE THIS FILE IS AUTO-GENERATED by the GenerateStandardLib.kt
// See: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
//"""

/**
 * Generates methods in the standard library which are mostly identical
 * but just using a different input kind.
 *
 * Kinda like mimicking source macros here, but this avoids the inefficiency of type conversions
 * at runtime.
 */
fun main(args: Array<String>) {
    require(args.size == 1, "Expecting Kotlin project home path as an argument")

    val outDir = File(File(args[0]), "libraries/stdlib/src/generated")
    require(outDir.exists(), "${outDir.getPath()} doesn't exist!")

    val jsCoreDir = File(args[0], "js/js.libraries/src/core")
    require(jsCoreDir.exists(), "${jsCoreDir.getPath()} doesn't exist!")

    generateDomAPI(File(jsCoreDir, "dom.kt"))
    generateDomEventsAPI(File(jsCoreDir, "domEvents.kt"))

    iterators().writeTo(File(outDir, "_Iterators.kt")) {
        buildFor(Iterators, null)
    }

    val arrays = arrays()
    val sumFunctions = PrimitiveType.values().map(::sumFunction).filterNotNull()
    (arrays + sumFunctions).writeTo(File(outDir, "_Arrays.kt")) {
        buildFor(Arrays, null)
    }

    for (primitive in PrimitiveType.values()) {
        (arrays + sumFunction(primitive)).filterNotNull().writeTo(File(outDir, "_${primitive.name}Arrays.kt")) {
            buildFor(PrimitiveArrays, primitive)
        }
    }

    (iterables().sort() + sumFunctions).writeTo(File(outDir, "_Iterables.kt")) {
        buildFor(Iterables, null)
    }

    collections().writeTo(File(outDir, "_Collections.kt")) {
        buildFor(Collections, null)
    }

    generateDownTos(File(outDir, "_DownTo.kt"), "package kotlin")
}

fun String.flat() = this.replaceAll(" ", "")

fun List<GenericFunction>.writeTo(file: File, builder: GenericFunction.() -> String) {
    println("Generating file: ${file.getPath()}")
    val its = FileWriter(file)

    its.use {
        its.append("package kotlin\n\n")
        its.append("$COMMON_AUTOGENERATED_WARNING\n\n")
        its.append("import java.util.*\n\n")
        for (t in this) {
            its.append(t.builder())
        }
    }
}

// Pretty hacky way to code generate; ideally we'd be using the AST and just changing the function prototypes
fun replaceGenerics(arrayName: String, it: String): String {
    return it.replaceAll(" <in T>", " ").replaceAll("<in T, ", "<").replaceAll("<T, ", "<").replaceAll("<T,", "<").
    replaceAll(" <T> ", " ").
    replaceAll("<T>", "<${arrayName}>").replaceAll("<in T>", "<${arrayName}>").
    replaceAll("\\(T\\)", "(${arrayName})").replaceAll("T\\?", "${arrayName}?").
    replaceAll("T,", "${arrayName},").
    replaceAll("T\\)", "${arrayName})").
    replaceAll(" T ", " ${arrayName} ")
}

