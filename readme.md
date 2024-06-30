## Json to XML convertor JAVA
In this task we have to create a java based cli application to covert JSON values to its respective XML values.

JSON : JavaScript Object Notation
XML : Extensible Markup Language

## Application used

**IDE :**  IntelliJ Community edition.


**Project Type:** Maven


**Used Dependencies:**

    1. com.googlecode.json-simple
    2. Org.json 
    3. com.google.code.gson


## Algorithm :
    1. Crate an empty java maven project.
    2. Add necessary dependencies to pom.xml.
    3. Create a input.txt file to write the test cases in valid JSON .
    4. Read the JSON as String from the input.txt using file reader.
    5. Convert the JSON string to map datatype.
    6. Extract the keys and values form the map.
    7. Use Doctument builder to create an XML doctument.
    8. Traverse through the Map.
    9. Check the json type and its map, then put the values to xml doctument.
    8. Print the xml doctument to console.
## Sample Input 1

    {
        "fibs" : true
    }

## Sample Output 1
    <object>
        <boolean name="fibs">true</boolean>
    </object>

## Sample Input 1

    {
    "fibs" : true,
    "name" : "Himanshu",
    "Age" : 21
    }


## Sample Output 2
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <object>
        <string name="name">Himanshu</string>
        <boolean name="fibs">true</boolean>
        <number name="Age">21.0</number>
    </object>