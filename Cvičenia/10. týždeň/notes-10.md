### SaxApp.java

```java
package saxapp;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class SaxApp {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);    // dolezite aby extrahoval localName
        
        SAXParser saxParser = spf.newSAXParser();
        
        saxParser.parse("test/Data.xml", new SaxHandler());
    }
}
```

---
### SaxHandler.java

- Vypis nazov vsetkych knih.
- Vypis nazov ak je edicia **SciFi**.
- Vypis nazov knih od **Maya**.
- Max cena v edicii **Stopy**.

```java
package saxapp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler{
    
    String text = null;
    
    String autor = null;
    String meno = null;
    Double cena;
    Double maxCena = 0.0;     // najdrahsej nacitanej knihy
    String maxMeno = null;    // najdrahsej nacitanej knihy
    String edicia = null;
    
    boolean knihaElement = false;
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text = new String(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        // Vypis nazov vsetkych knih
        if ("meno".equals(qName) && knihaElement){
            System.out.println("" + text);
            knihaElement = false;
        }
        
        // Vypis nazov ak je edicia SciFi
        if ("meno".equals(qName) && knihaElement && "SciFi".equals(edicia)){
            System.out.println("" + text);
        }

        // Vypis nazov knih od Maya
        if ("meno".equals(qName) && knihaElement){
            meno = text;
        }
        if ("autor".equals(qName)){
            autor = text;
        }

        if ("cena".equals(qName)){
            cena = Double.valueOf(text);
        }

        // Precital som koniec elementu kniha
        if ("kniha".equals(qName)){

            if ("Orwell".equals(autor)){
                System.out.println("" + meno);
            }

        // Max cena v edicii stopy
        if ((cena != null) && "Stopy".equals(edicia)){
            if (cena > maxCena){

                maxCena = cena;
                maxMeno = meno;
            }
        }

        meno = null;
        autor = null;
        cena = null;
        edicia = null;
        knihaElement = false;
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        
        if (maxMeno != null){
            System.out.println("Najdrahsia kniha : " + maxMeno + " = " + maxCena);
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        // treba sledovat, ci sme v knihe
        // rozne elementy mozu mat rovnake tagy
        if ("kniha".equals(qName)){
            knihaElement = true;
            
            edicia = attributes.getValue("edicia");
        }
    }
}
```

---
### Data.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<knihkupectvo>
    <meno>Panta Rhei</meno>
    <kniha edicia="SciFi">
        <meno>Nadacia</meno>
        <cena>6.40</cena>
        <autor>Asimov</autor>
    </kniha>
    <kniha>
        <meno>Stary Zakon</meno>
        <cena>12.00</cena>
    </kniha>
    <kniha edicia="Stopy">
        <meno>Pustou</meno>
        <cena>11.95</cena>
        <autor>May</autor>
    </kniha>
    <kniha edicia="Stopy">
        <meno>Winetou</meno>
        <cena>8.9</cena>
        <autor>May</autor>
    </kniha>
    <kniha edicia="SciFi">
        <meno>1984</meno>
        <autor>Orwell</autor>
        <cena>3.80</cena>
    </kniha>
    <kniha edicia="SciFi">
        <cena>13.80</cena>
        <autor>Orwell</autor>
        <meno>Farma</meno>
    </kniha>
</knihkupectvo>
```