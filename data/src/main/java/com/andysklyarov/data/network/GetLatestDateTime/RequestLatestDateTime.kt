package com.andysklyarov.data.network.GetLatestDateTime

import org.simpleframework.xml.*

@Root(name = "soap:Envelope")
@NamespaceList(
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")
)
data class RequestLatestDateTimeEnvelope @JvmOverloads constructor(
    @get:Element(name = "Body")
    @set:Element(name = "Body")
    @get:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    @set:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "soap")
    var body: RequestLatestDateTimeBody? = null
)

@Root(name = "soap:Body")
data class RequestLatestDateTimeBody @JvmOverloads constructor(
    @get:Element(name = "GetLatestDateTime")
    @set:Element(name = "GetLatestDateTime")
    var latestDateTimeData: RequestLatestDateTimeData? = null
)

@Root(name = "GetLatestDateTime")
data class RequestLatestDateTimeData @JvmOverloads constructor(
    @get:Attribute(name = "xmlns")
    @set:Attribute(name = "xmlns")
    var xmlns: String? = "http://web.cbr.ru/"
)