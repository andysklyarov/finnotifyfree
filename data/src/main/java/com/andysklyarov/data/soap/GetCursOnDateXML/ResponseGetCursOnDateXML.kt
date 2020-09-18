package com.andysklyarov.data.soap.GetCursOnDateXML

import org.simpleframework.xml.*

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class ResponseGetCursOnDateXMLEnvelope @JvmOverloads constructor(
    @get:Element(name = "Body")
    @set:Element(name = "Body")
    @get:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    @set:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    var body: ResponseGetCursOnDateXMLBody? = null
)


@Root(name = "soap:Body")
data class ResponseGetCursOnDateXMLBody @JvmOverloads constructor(
    @get:Element(name = "GetCursOnDateXMLResponse")
    @set:Element(name = "GetCursOnDateXMLResponse")
    @get:Namespace(reference = "http://web.cbr.ru/")
    @set:Namespace(reference = "http://web.cbr.ru/")
    var cursOnDateXMLResponse: ResponseGetCursOnDateXMLResponse? = null
)


@Root(name = "GetCursOnDateXMLResponse")
@Namespace(reference = "http://web.cbr.ru/")
data class ResponseGetCursOnDateXMLResponse @JvmOverloads constructor(
    @get:Element(name = "GetCursOnDateXMLResult")
    @set:Element(name = "GetCursOnDateXMLResult")
    var getCursOnDateXMLResult: ResponseGetCursOnDateXMLResult? = null
)


@Root(name = "GetCursOnDateXMLResult")
data class ResponseGetCursOnDateXMLResult @JvmOverloads constructor(
    @get:Element(name = "ValuteData")
    @set:Element(name = "ValuteData")
    @get:Namespace(reference = "")
    @set:Namespace(reference = "")
    var valuteData: ResponseValuteData? = null
)


@Root(name = "ValuteData")
@Namespace(reference = "")
data class ResponseValuteData @JvmOverloads constructor(
    @get:ElementList(name = "ValuteCursOnDate", inline = true)
    @set:ElementList(name = "ValuteCursOnDate", inline = true)
    var valuteCurses: List<ResponseValuteCursOnDate>? = null,

    @get:Attribute(name = "OnDate")
    @set:Attribute(name = "OnDate")
    var onDate: String? = null
)


@Root(name = "ValuteCursOnDate")
data class ResponseValuteCursOnDate @JvmOverloads constructor(
    @get:Element(name = "Vname")
    @set:Element(name = "Vname")
    var valName: String = "",

    @get:Element(name = "Vnom")
    @set:Element(name = "Vnom")
    var nom: String = "",

    @get:Element(name = "Vcurs")
    @set:Element(name = "Vcurs")
    var curs: String = "",

    @get:Element(name = "Vcode")
    @set:Element(name = "Vcode")
    var code: String = "",

    @get:Element(name = "VchCode")
    @set:Element(name = "VchCode")
    var chCode: String = ""
)