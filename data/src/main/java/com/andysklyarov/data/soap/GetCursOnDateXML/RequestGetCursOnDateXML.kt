package com.andysklyarov.data.soap.GetCursOnDateXML

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")
)
data class RequestGetCursOnDateXMLEnvelope @JvmOverloads constructor(
    @get:Element(name = "Body")
    @set:Element(name = "Body")
    @get:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    @set:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    var body: RequestGetCursOnDateXMLBody? = null
)


@Root(name = "soap:Body")
data class RequestGetCursOnDateXMLBody @JvmOverloads constructor(
    @get:Element(name = "GetCursOnDateXML")
    @set:Element(name = "GetCursOnDateXML")
    @get:Namespace(reference = "http://web.cbr.ru/")
    @set:Namespace(reference = "http://web.cbr.ru/")
    var getCursOnDateXML: RequestGetCursOnDateXMLData? = null
)


@Root(name = "GetCursOnDateXML")
@Namespace(reference = "http://web.cbr.ru/")
data class RequestGetCursOnDateXMLData @JvmOverloads constructor(
    @get:Element(name = "On_date")
    @set:Element(name = "On_date")
    var dateTime: String? = null
)