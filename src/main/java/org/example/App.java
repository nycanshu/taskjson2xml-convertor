
package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class App {
    static ArrayList<String> keys = new ArrayList<>();
    static ArrayList<Object> values = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        List<String> jsonInputs = fileInput();

        convertToXml(jsonInputs);
    }

    public static List<String> fileInput() {
        List<String> jsonInputs = new ArrayList<>();
        try {
            File myObj = new File("E:\\task\\src\\main\\java\\org\\example\\input.txt");
            Scanner myReader = new Scanner(myObj);

            StringBuilder currentJson = new StringBuilder();
            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();

                if (!currentLine.trim().isEmpty()) {
                    currentJson.append(currentLine);
                } else {
                    jsonInputs.add(currentJson.toString());
                    currentJson.setLength(0); // Reset for the next JSON object
                }
            }

            if (currentJson.length() > 0) {
                jsonInputs.add(currentJson.toString());
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return jsonInputs;
    }

    public static Map<String, ?> jsonToMap(String str) {
        return new Gson().fromJson(str, new TypeToken<HashMap<Object, Object>>() {}.getType());
    }

    public static void getKeyValueOfMap(Map<String, ?> map) {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
    }

    public static void convertToXml(List<String> jsonInputs) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        for (String jsonInput : jsonInputs) {
            Map<String, ?> map = jsonToMap(jsonInput);
            getKeyValueOfMap(map);

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("object");
            doc.appendChild(rootElement);

            generateContent(keys, values, rootElement, doc);

            writeXml(doc, System.out);

            keys.clear();
            values.clear();
        }
    }

    public static void generateContent(ArrayList<String> keys, ArrayList<Object> values, Element rootElement, Document doc) {
        for (int i = 0; i < keys.size() && i < values.size(); i++) {
            Element element = doc.createElement(decideTagType(values.get(i)));
            rootElement.appendChild(element);
            element.setAttribute("name", keys.get(i));

            if (isArrayType(values.get(i))) {
                List<Object> nestedArray = (List<Object>) values.get(i);
                for (Object item : nestedArray) {
                    if (item instanceof Map) {
                        Element nestedObjectElement = doc.createElement("object");
                        generateContent((ArrayList<String>) Arrays.asList(keys.get(i)), (ArrayList<Object>) Collections.singletonList(item), nestedObjectElement, doc);
                        element.appendChild(nestedObjectElement);
                    } else {
                        Element nestedElement = doc.createElement(decideTagType(item));
                        nestedElement.setTextContent(item.toString());
                        element.appendChild(nestedElement);
                    }
                }
            } else if (values.get(i) instanceof Map) {
                Element nestedObjectElement = doc.createElement("object");
                Map<String, Object> nestedMap = (Map<String, Object>) values.get(i);
                generateContent(new ArrayList<>(nestedMap.keySet()), new ArrayList<>(nestedMap.values()), nestedObjectElement, doc);
                element.appendChild(nestedObjectElement);
            } else {
                element.setTextContent(values.get(i).toString());
            }
        }
    }




//    public static void nestedPrint(String tag, ArrayList<Object> arr, Document doc, Element rootElement) {
//        for (Object val : arr) {
//            Element staff2 = doc.createElement(decideTagType(val.getClass().getName()));
//
//            rootElement.appendChild(staff2);
//
//            Element name2 = doc.createElement("number");
//            name2.setTextContent(val.toString());
//            staff2.appendChild(name2);
//        }
//    }

    public static boolean isArrayType(Object obj) {
        return obj instanceof List;
    }

    public static String decideTagType(Object obj) {
        if (obj instanceof Double || obj instanceof Float
                || obj instanceof Integer || obj instanceof Long) {
            return "number";
        } else if (obj instanceof String) {
            return "string";
        } else if (obj instanceof Boolean) {
            return "boolean";
        } else if (obj instanceof List) {
            return "arrays";
        } else if (obj instanceof Map) {
            return "object";
        } else {
            return "Unidentified-type";
        }
    }


    private static void writeXml(Document doc, PrintStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}
