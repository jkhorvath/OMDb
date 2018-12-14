///////////////////////////////////////////////////////////////////////
//
//     Copyright (c) Cisco Systems, Inc. 2018 All rights reserved
//                     .:|:.:|:.
//
///////////////////////////////////////////////////////////////////////
// File                 :  HeadersContentsResponse.java
//
// Notes                :
//
// Updates              :
//                               new - Superuser - December 13, 2018
///////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class HeadersContentsResponse
//
///////////////////////////////////////////////////////////////////////

package com.cisco.omdb.util;

import java.util.Map;

import javax.json.JsonStructure;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A HeadersContentsResponse class is used for holding the JSON response to a web URL request
 *
 * @author  Joe Horvath (joshorva@cisco.com)
 */

public class HeadersContentsResponse extends Object
{
    private final static String          CLASS_NAME = HeadersContentsResponse.class.getName ();

    // log4j logger object
    @SuppressWarnings ("unused")
    private final static Logger          logger     = LogManager.getLogger (HeadersContentsResponse.CLASS_NAME);

    private final JsonStructure          contents;
    private final Map < String, String > headers;
    private final HttpResponse           response;

    /**
     * Public constructor for HeadersContentsResponse class
     *
     * @param _headers - A Map < String, String > representing ...
     * @param _contents - A JsonStructure representing ...
     * @param _response - A HttpResponse representing ...
     */
    public HeadersContentsResponse (final Map < String, String > _headers, final JsonStructure _contents, final HttpResponse _response)
    {
        super ();

        headers = _headers;
        contents = _contents;
        response = _response;
    }

    /**
     * Getter for HeadersContentsResponse variable contents
     *  Returns the current value of HeadersContentsResponse's class variable contents
     *
     * @return - The current value for HeadersContentsResponse's class variable contents as a(n) JsonStructure
     */
    public JsonStructure getContents ()
    {
        return (contents);
    }

    /**
     * Getter for HeadersContentsResponse variable headers
     *  Returns the current value of HeadersContentsResponse's class variable headers
     *
     * @return - The current value for HeadersContentsResponse's class variable headers as a(n) Map < String, String >
     */
    public Map < String, String > getHeaders ()
    {
        return (headers);
    }

    public String getHeaderValue (final String name)
    {
        return (headers.get (name));
    }

    /**
     * Getter for HeadersContentsResponse variable response
     *  Returns the current value of HeadersContentsResponse's class variable response
     *
     * @return - The current value for HeadersContentsResponse's class variable response as a(n) HttpResponse
     */
    public HttpResponse getResponse ()
    {
        return (response);
    }

    /**
     * Conversion to String for HeadersContentsResponse class
     *
     * @return - A formatted String describing class HeadersContentsResponse
     */
    @Override
    public String toString ()
    {
        return (super.toString () + "[" + HeadersContentsResponse.CLASS_NAME + ": " +
                "Map < String, String > headers = '" + headers + "', " +
                "JsonStructure contents = '" + contents + "', " +
                "HttpResponse response = '" + response + "', " + "]");
    }
}
