//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.14 at 01:20:47 PM IDT 
//


package Generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ETT-TimeTable"/>
 *         &lt;element ref="{}ETT-EvolutionEngine"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ettTimeTable",
    "ettEvolutionEngine"
})
@XmlRootElement(name = "ETT-Descriptor")
public class ETTDescriptor {

    @XmlElement(name = "ETT-TimeTable", required = true)
    protected ETTTimeTable ettTimeTable;
    @XmlElement(name = "ETT-EvolutionEngine", required = true)
    protected ETTEvolutionEngine ettEvolutionEngine;

    /**
     * Gets the value of the ettTimeTable property.
     * 
     * @return
     *     possible object is
     *     {@link ETTTimeTable }
     *     
     */
    public ETTTimeTable getETTTimeTable() {
        return ettTimeTable;
    }

    /**
     * Sets the value of the ettTimeTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link ETTTimeTable }
     *     
     */
    public void setETTTimeTable(ETTTimeTable value) {
        this.ettTimeTable = value;
    }

    /**
     * Gets the value of the ettEvolutionEngine property.
     * 
     * @return
     *     possible object is
     *     {@link ETTEvolutionEngine }
     *     
     */
    public ETTEvolutionEngine getETTEvolutionEngine() {
        return ettEvolutionEngine;
    }

    /**
     * Sets the value of the ettEvolutionEngine property.
     * 
     * @param value
     *     allowed object is
     *     {@link ETTEvolutionEngine }
     *     
     */
    public void setETTEvolutionEngine(ETTEvolutionEngine value) {
        this.ettEvolutionEngine = value;
    }

}
