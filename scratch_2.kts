/*
{
    "key1": "value1",
    "key2": "value2"
}
 */

fun parseTwitchSon(twitchSon: String): Map<String, String> {
    val innerString = twitchSon.trim().substring(1, twitchSon.length - 1).trim()
    println(innerString)

    val listOfKeys = mutableListOf<StringBuilder>()
    val listOfValues = mutableListOf<StringBuilder>()
    var parsingKey = false
    var parsingValue = false
    var parsedKey = false
    var currentIndex = 0
    val outputMap = mutableMapOf<String, String>()
    innerString.forEachIndexed { i, char ->
        println("Parsing char $char")
        println("parsingKey $parsingKey, parsingValue $parsingValue, parsedKey $parsedKey, currentIndex $currentIndex")
        if (char == '"' && !parsingKey && !parsingValue && !parsedKey) {
            // Encounter " while not parsing anything -> first quote starting key
            parsingKey = true
            listOfKeys.add(StringBuilder())
        } else if (char == '"' && parsingKey && !parsingValue && !parsedKey) {
            // Encounter " while parsing key -> end quote in key
            // but if previous char is a slash then include it in key and keep going
            if (i > 0 && innerString[i - 1] == '\\') {
                // the quote was escaped so append it to the key
                listOfKeys[currentIndex] = listOfKeys[currentIndex].append(char)
            } else {
                parsingKey = false
                parsedKey = true
            }
        } else if (char == '"' && !parsingKey && !parsingValue && parsedKey) {
            // Encounter " while not parsing anything but key has already been parsed -> first quote starting value
            parsingValue = true
            listOfValues.add(StringBuilder())
        } else if (char == '"' && !parsingKey && parsingValue && parsedKey) {
            // Encounter " while parsing value -> end quote in value
            // but if previous char is a slash then include it in value and keep going
            if (i > 0 && innerString[i - 1] == '\\') {
                // the quote was escaped so append it to the key
                println("adding char to value")
                listOfValues[currentIndex] = listOfValues[currentIndex].append(char)
            } else {
                // Finished with this key value pair so add it to the output map
                val key = listOfKeys[currentIndex].toString()
                outputMap[key] = listOfValues[currentIndex].toString()
                parsingValue = false
                parsedKey = false
                currentIndex++
            }
        } else if (parsingKey && !parsingValue && !parsedKey) {
            // Currently parsing the key and the char is not a ", so append it to the key
            println("adding char to key")
            listOfKeys[currentIndex] = listOfKeys[currentIndex].append(char)
        } else if (!parsingKey && parsingValue && parsedKey) {
            // Currently parsing the value and the char is not a ", so append it to the value
            println("adding char to value")
            listOfValues[currentIndex] = listOfValues[currentIndex].append(char)
        } else {
            // skipping this char
            println("skipping this char")
        }
    }

    println("Finished parsing")
    println("listOfKeys $listOfKeys")
    println("listOfValues $listOfValues")

    return outputMap
}

fun main() {
    val twitchSon =
        "{\n" +
                "    \"key\\\"1\": \"value1\",\n" +
                "    \"key2\": \"valu\\\"e2\"\n" +
                "}"
    parseTwitchSon(twitchSon)
}

main()