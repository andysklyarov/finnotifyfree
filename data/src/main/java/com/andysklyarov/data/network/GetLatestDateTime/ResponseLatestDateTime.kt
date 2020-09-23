package com.andysklyarov.data.network.GetLatestDateTime

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
)
data class ResponseLatestDateTimeEnvelope @JvmOverloads constructor(
    @get:Element(name = "Body")
    @set:Element(name = "Body")
    @get:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    @set:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    var body: ResponseLatestDateTimeBody? = null
)

@Root(name = "soap:Body")
data class ResponseLatestDateTimeBody @JvmOverloads constructor(
    @get:Element(name = "GetLatestDateTimeResponse")
    @set:Element(name = "GetLatestDateTimeResponse")
    @get:Namespace(reference = "http://web.cbr.ru/")
    @set:Namespace(reference = "http://web.cbr.ru/")
    var latestDateTimeDataResponse: ResponseLatestDateTimeData? = null
)

@Root(name = "GetLatestDateTimeResponse")
@Namespace(reference = "http://web.cbr.ru/")
data class ResponseLatestDateTimeData @JvmOverloads constructor(
    @get:Element(name = "GetLatestDateTimeResult")
    @set:Element(name = "GetLatestDateTimeResult")
    var dateTime: String? = null
)