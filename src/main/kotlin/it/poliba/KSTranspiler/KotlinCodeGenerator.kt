package it.poliba.KSTranspiler

import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.ArrayList
import java.util.StringJoiner

val kotlinGroup: STGroup = STGroupFile("src/main/antlr/KotlinTemplate.stg")

fun AstScript.generateKotlinCode(depth: Int = 0): String{
    return statement.joinToString("\n") { it.generateKotlinCode(depth) }
}

fun Declaration.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is PropertyDeclaration -> this.generateKotlinCode(depth)
        is WidgetDeclaration -> this.generateKotlinCode(depth)
        is FunctionDeclaration -> this.generateKotlinCode(depth)
        is ClassDeclaration -> this.generateKotlinCode(depth)
        is DataClassDeclaration -> this.generateKotlinCode(depth)
        is PrimaryConstructor -> this.generateKotlinCode(depth)
    }
}

fun PrimaryConstructor.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}init ${this.body.generateKotlinCode(depth)}"
}

fun ClassDeclaration.generateKotlinCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateKotlinCode()}
    }
    var bodyStatement = body.map { it.generateKotlinCode(depth+1) }
    var bodyString = bodyStatement.joinToString("\n"){"$it"}
    var constructor = ""
    (body.firstOrNull { it is PrimaryConstructor } as PrimaryConstructor).parameters?.let {
        if(it.isNotEmpty()){
            var params = it.joinToString(",\n") { it.generateKotlinCode() }
            constructor = "(\n$params\n)"
        }
    }
    var res = "${getPrefix(depth)}class $name$constructor$baseClassesString {\n$bodyString\n${getPrefix(depth)}}"
    return res
}

fun DataClassDeclaration.generateKotlinCode(depth: Int = 0): String{
    var baseClassesString = ""
    if(baseClasses.isNotEmpty()){
        baseClassesString += ": "
        baseClassesString += baseClasses.joinToString(", "){it.generateKotlinCode()}
    }
    var bodyStatement = body.map { it.generateKotlinCode(depth+1) }
    var bodyString = bodyStatement.joinToString("\n"){"$it"}
    var constructor = ""
    if(propertyList.isNotEmpty()){
        var params = propertyList.joinToString(",\n") { it.generateKotlinCode(depth +1) }
        constructor = "(\n$params\n)"
    }

    var res = "${getPrefix(depth)}data class $name$constructor$baseClassesString {\n$bodyString\n${getPrefix(depth)}}"
    return res
}
fun Statement.generateKotlinCode(depth: Int = 0): String {
    return when (this) {
        is PropertyDeclaration -> this.generateKotlinCode(depth)
        is Assignment -> this.generateKotlinCode(depth)
        is Print -> this.generateKotlinCode(depth)
        is IfExpression -> this.generateKotlinCode(depth)
        is ForExpression -> this.generateKotlinCode(depth)
        is Expression -> this.generateKotlinCode(depth)
        is FunctionDeclaration -> this.generateKotlinCode(depth)
        is Declaration -> this.generateKotlinCode(depth)
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FunctionDeclaration.generateKotlinCode(depth: Int = 0): String{
    val returnType = if(this.returnType != null) ": ${this.returnType.generateKotlinCode()}" else ""
    return "${getPrefix(depth)}fun ${this.id}(${this.parameters.joinToString(", "){it.generateKotlinCode()}})"+returnType +
            "${this.body.generateKotlinCode(depth)}"
}


fun FunctionParameter.generateKotlinCode(): String{
    return "${this.id}: ${this.type.generateKotlinCode()}"
}

fun Assignment.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}${variable.generateKotlinCode()} = ${value.generateKotlinCode()}"
}

fun Print.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}print(${value.generateKotlinCode()})"
}

fun IfExpression.generateKotlinCode(depth: Int = 0): String{
    var result =  "${getPrefix(depth)}if(${condition.generateKotlinCode()}) "+
            "${body.generateKotlinCode(depth)}"
    elseBranch?.let {
        when (it){
            is IfExpression ->result += "else "+ it.generateKotlinCode(depth)
            else -> result += "else ${it.generateKotlinCode()}"

        }
    }
    return result
}
fun ForExpression.generateKotlinCode(depth: Int = 0): String{
    return "${getPrefix(depth)}for(${varName} in ${range.generateKotlinCode()}) ${body.generateKotlinCode(depth)}"
}

fun ControlStructureBody.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is Block -> this.generateKotlinCode(depth)
        is Statement -> this.generateKotlinCode(depth)
        else -> throw Exception("Not implemented")
    }
}

fun Block.generateKotlinCode(depth: Int = 0): String{
    return "{\n${this.body.joinToString("\n") { it.generateKotlinCode(depth+1)}}\n${getPrefix(depth)}}"
}
fun PropertyDeclaration.generateKotlinCode(depth: Int = 0): String{
    var prefix = if (mutable) "var" else "val"
    var type = type.generateKotlinCode()
    var value = value?.let {  " = ${value.generateKotlinCode()}"} ?: ""
    return "${getPrefix(depth)}$prefix $varName:$type$value"
}

fun Expression.generateKotlinCode(depth: Int=0) : String = when (this) {
    is IntLit -> "${getPrefix(depth)}${this.value}"
    is DoubleLit -> "${getPrefix(depth)}${this.value}"
    is DpLit -> "${getPrefix(depth)}${this.value}.dp"
    is VarReference -> "${getPrefix(depth)}${this.varName}"
    is BinaryExpression -> this.generateKotlinCode(depth)
    is LogicalExpression -> this.generateKotlinCode(depth)
    is StringLit -> "${getPrefix(depth)}\"${this.value}\""
    is BoolLit -> "${getPrefix(depth)}${this.value}"
    is FunctionCall -> "${getPrefix(depth)}${this.name}(${this.parameters.map { it.generateKotlinCode() }.joinToString(", " )})"
    is RangeExpression -> "${getPrefix(depth)}${this.leftExpression.generateKotlinCode()}..${this.rightExpression.generateKotlinCode()}"
    is ListExpression -> "${getPrefix(depth)}[${this.items.map { it.generateKotlinCode() }.joinToString(", ")}]"
    is ColorLit -> this.generateKotlinCode(depth)
    is FontWeightLit -> this.generateKotlinCode(depth)
    is ReturnExpression -> "${getPrefix(depth)}return ${this.returnExpression.generateKotlinCode()}"
    is TextComposableCall -> this.generateKotlinCode(depth)
    is DividerComposableCall -> this.generateKotlinCode(depth)
    is SpacerComposableCall -> this.generateKotlinCode(depth)
    is ColumnComposableCall -> this.generateKotlinCode(depth)
    is RowComposableCall -> this.generateKotlinCode(depth)
    is HorizontalAlignment -> this.generateKotlinCode(depth)
    is VerticalAlignment -> this.generateKotlinCode(depth)
    is ButtonComposableCall -> this.generateKotlinCode(depth)
    is AccessExpression -> this.generateKotlinCode(depth)
    is ThisExpression -> this.generateKotlinCode(depth)
    is AspectRatioLit -> this.generateKotlinCode()
    is ImageComposableCall -> this.generateKotlinCode(depth)
    is ZStackComposableCall -> this.generateKotlinCode(depth)
    //is KotlinParser.ParenExpressionContext -> expression().toAst(considerPosition)
    //is KotlinParser.TypeConversionContext -> TypeConversion(expression().toAst(considerPosition), targetType.toAst(considerPosition), toPosition(considerPosition))
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun DividerComposableCall.generateKotlinCode(depth: Int=0): String{
    var params: ArrayList<String> = arrayListOf()

    color?.let { params.add("color = ${it.generateKotlinCode()}") }
    frame?.let { params.add("thickness = ${it.height?.generateKotlinCode()}") }
    zIndex?.let {
        params.add("modifier = Modifier.zIndex(${it.generateKotlinCode(0)})")
    }
    print("SIZE: ${params.size}")
    var paramString = params.joinToString(", ")
    return "${getPrefix(depth)}Divider($paramString)"
}
fun ZStackComposableCall.generateKotlinCode(depth: Int=0): String{
    return "${getPrefix(depth)}Box()${body.generateKotlinCode(depth)}"
}
fun ButtonComposableCall.generateKotlinCode(depth: Int=0): String{
    var modifier = ""
    zIndex?.let {
        modifier = ", modifier = Modifier.zIndex(${it.generateKotlinCode(0)})"
    }
    return "${getPrefix(depth)}Button(onClick = ${this.action.generateKotlinCode(depth)}$modifier)${this.body.generateKotlinCode(depth)} "
}
fun SpacerComposableCall.generateKotlinCode(depth: Int=0): String{
    val suffix = size?.let { "${it.generateKotlinCode()}" } ?: ""
    val zIndex = zIndex?.let {
        ".zIndex(${it.generateKotlinCode(0)})"
    } ?: ""
    var modifier = ""
    if(zIndex != "" || suffix != ""){
        modifier = "modifier = Modifier$zIndex$suffix"
    }
    return "${getPrefix(depth)}Spacer($modifier)"
}

fun Frame.generateKotlinCode(depth: Int=0): String{
    var width = width?.let { ".width(${width.generateKotlinCode()})" }
    var height = height?.let { ".height(${height.generateKotlinCode()})" }

    val params = listOf(width, height).filterNotNull()

    return "${params.joinToString("")}"
}

fun Type.generateKotlinCode(depth: Int=0) : String = when (this) {
    is IntType -> "Int"
    is DoubleType -> "Double"
    is StringType -> "String"
    is BoolType -> "Boolean"
    is RangeType -> "ClosedRange<${this.type.generateKotlinCode()}>"
    is ListType -> "[${this.itemsType.generateKotlinCode()}]"
    is ArrayType -> "Array<${this.itemsType.generateKotlinCode()}>"
    is UserType -> "${this.name}"
    is AspectRatioType -> "ContentScale"
    is ColorType -> "Color"
    is FontWeightType -> "FontWeight"
    is DpType -> "Dp"
    is HorizontalAlignmentType -> "Alignment.Horizontal"
    is VerticalAlignmentType -> "Alignment.Vertical"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun BinaryExpression.generateKotlinCode(depth: Int=0): String = when(this) {
    is SumExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} + ${right.generateKotlinCode()}"
    is SubtractionExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} - ${right.generateKotlinCode()}"
    is MultiplicationExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} * ${right.generateKotlinCode()}"
    is DivisionExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} / ${right.generateKotlinCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun LogicalExpression.generateKotlinCode(depth: Int=0): String = when(this) {
    is EqualExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} == ${right.generateKotlinCode()}"
    is NotEqualExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} != ${right.generateKotlinCode()}"
    is AndExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} && ${right.generateKotlinCode()}"
    is OrExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} || ${right.generateKotlinCode()}"
    is LTEqualExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} <= ${right.generateKotlinCode()}"
    is GTEqualExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} >= ${right.generateKotlinCode()}"
    is LessThanExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} < ${right.generateKotlinCode()}"
    is GreaterThanExpression -> "${getPrefix(depth)}${left.generateKotlinCode()} > ${right.generateKotlinCode()}"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}
fun TextComposableCall.generateKotlinCode(depth: Int=0): String{
    val parameters = arrayListOf<String>()
    parameters.add(this.value.generateKotlinCode())

    this.color?.generateKotlinCode()?.let {
        parameters.add("color = $it")
    }

    this.fontWeight?.generateKotlinCode()?.let {
        parameters.add("fontWeight = $it")
    }
    zIndex?.let {
        parameters.add("modifier = Modifier.zIndex(${it.generateKotlinCode(0)})")
    }
    return "${getPrefix(depth)}Text(${parameters.joinToString(", ") { it }})"
}

fun ColorLit.generateKotlinCode(depth: Int=0): String = when(this){
    is ColorBlack -> "${getPrefix(depth)}Color.Black"
    is ColorBlue -> "${getPrefix(depth)}Color.Blue"
    is ColorCyan -> "${getPrefix(depth)}Color.Cyan"
    is ColorGray -> "${getPrefix(depth)}Color.Gray"
    is ColorGreen -> "${getPrefix(depth)}Color.Green"
    is ColorRed -> "${getPrefix(depth)}Color.Red"
    is ColorWhite -> "${getPrefix(depth)}Color.White"
    is ColorYellow -> "${getPrefix(depth)}Color.Yellow"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun FontWeightLit.generateKotlinCode(depth: Int=0): String = when(this){
    is FontWeightBlack -> "${getPrefix(depth)}FontWeight.Black"
    is FontWeightExtraBold -> "${getPrefix(depth)}FontWeight.ExtraBold"
    is FontWeightBold -> "${getPrefix(depth)}FontWeight.Bold"
    is FontWeightSemiBold -> "${getPrefix(depth)}FontWeight.SemiBold"
    is FontWeightMedium -> "${getPrefix(depth)}FontWeight.Medium"
    is FontWeightNormal -> "${getPrefix(depth)}FontWeight.Normal"
    is FontWeightLight -> "${getPrefix(depth)}FontWeight.Light"
    is FontWeightExtraLight -> "${getPrefix(depth)}FontWeight.ExtraLight"
    is FontWeightThin -> "${getPrefix(depth)}FontWeight.Thin"
    else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
}

fun WidgetDeclaration.generateKotlinCode(depth: Int=0): String {
    val convertedProperties = this.parameters.joinToString(", ") { "${it.id}: ${it.type.generateKotlinCode()}" }
    val body = "${body.generateKotlinCode()}"
    return "${getPrefix(depth)}@Composable\n${getPrefix(depth)}fun $id($convertedProperties)${body}"
}

fun ColumnComposableCall.generateKotlinCode(depth: Int=0): String{

    val arguments = arrayListOf<String>()

    horizontalAlignment?.generateKotlinCode()?.let {
        arguments.add("horizontalAlignment = $it")
    }

    spacing?.generateKotlinCode()?.let {
        arguments.add("verticalArrangement = Arrangement.spacedBy($it)")
    }

    val bodyString = body.generateKotlinCode(depth)
    var modifier = ""
    if (scrollable) modifier = "modifier = Modifier.verticalScroll(rememberScrollState())"
    zIndex?.let {
        var indexSuffix = ".zIndex(${it.generateKotlinCode()})"
        if(modifier == "") modifier = "modifier = Modifier"
        modifier += indexSuffix
    }
    if(modifier != ""){
        arguments.add(modifier)
    }

    val parameters = arguments.joinToString(",\n${getPrefix(depth+1)}")

    return "${getPrefix(depth)}Column(\n${getPrefix(depth+1)}$parameters\n${getPrefix(depth)})$bodyString"
}

fun RowComposableCall.generateKotlinCode(depth: Int=0): String{

    val arguments = arrayListOf<String>()

    verticalAlignment?.generateKotlinCode()?.let {
        arguments.add("verticalAlignment = $it")
    }

    spacing?.generateKotlinCode()?.let {
        arguments.add("horizontalArrangement = Arrangement.spacedBy($it)")
    }

    val bodyString = body.generateKotlinCode(depth)
    var modifier = ""
    if (scrollable) modifier = "modifier = Modifier.horizontalScroll(rememberScrollState())"
    zIndex?.let {
        var indexSuffix = ".zIndex(${it.generateKotlinCode()})"
        if(modifier == "") modifier = "modifier = Modifier"
        modifier += indexSuffix
    }
    if(modifier != ""){
        arguments.add(modifier)
    }
    val parameters = arguments.joinToString(",\n${getPrefix(depth+1)}")
    return "${getPrefix(depth)}Row(\n${getPrefix(depth+1)}$parameters\n${getPrefix(depth)})$bodyString"
}

fun AspectRatioLit.generateKotlinCode(depth: Int = 0): String{
    var res = when(this){
        is ContentFit -> "ContentScale.Fit"
        is ContentFill -> "ContentScale.FillWidth"
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
    return "$res" //TODO: ADD PREFIX
}


fun ImageComposableCall.generateKotlinCode(depth: Int = 0): String{
    var imageName = this.value.generateKotlinCode()
    var painter = "painter = painterResource(id = getResources().getIdentifier($imageName, \"drawable\", context.getPackageName()))"

    var parameters = arrayListOf<String>()
    var modifierParams = arrayListOf<String>()
        //TODO: resizable
    this.aspectRatio?.generateKotlinCode()?.let {
        parameters.add("contentScale = $it")
    }
    if(resizable){
        modifierParams.add(".fillMaxSize()")
    }
    zIndex?.let {
        modifierParams.add(".zIndex(${it.generateKotlinCode()})")
    }
    if(modifierParams.isNotEmpty()){
        var modifierParamsString = modifierParams.joinToString("")
        parameters.add("modifier = Modifier$modifierParamsString")
    }
    if(parameters.isEmpty()){
        return "${getPrefix(depth)}Image($painter)"
    }else{
        var param = parameters.joinToString ( ",\n${getPrefix(depth)}" )
        return "${getPrefix(depth)}Image(\n${getPrefix(depth)}$painter,\n${getPrefix(depth)}$param)"

    }
}

fun HorizontalAlignment.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is StartAlignment -> "${getPrefix(depth)}Alignment.Start"
        is EndAlignment -> "${getPrefix(depth)}Alignment.End"
        is CenterHorizAlignment -> "${getPrefix(depth)}Alignment.Center"
    }
}

fun VerticalAlignment.generateKotlinCode(depth: Int = 0): String{
    return when(this){
        is TopAlignment -> "${getPrefix(depth)}Alignment.Top"
        is BottomAlignment -> "${getPrefix(depth)}Alignment.Bottom"
        is CenterVerticallyAlignment -> "${getPrefix(depth)}Alignment.Center"
    }
}
fun AccessExpression.generateKotlinCode(depth: Int = 0): String{
    return "${prefix.generateKotlinCode(depth)}${accessOperator.generateKotlinCode()}${child.generateKotlinCode()}"
}

fun AccessOperator.generateKotlinCode(depth: Int=0): String{
    return when (this){
        is DotOperator -> "."
        is ElvisOperator -> "?."
    }
}
fun ThisExpression.generateKotlinCode(depth: Int=0): String{
    return "${getPrefix(depth)}this"
}
