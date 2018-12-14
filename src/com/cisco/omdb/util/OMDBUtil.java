///////////////////////////////////////////////////////////////////////
//
//     Copyright (c) Cisco Systems, Inc. 2018 All rights reserved
//                     .:|:.:|:.
//
///////////////////////////////////////////////////////////////////////
// File                 :  OMDBUtil.java
//
// Notes                :
//
// Updates              :
//                               new - December 13, 2018
///////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class OMDBUtil
//
///////////////////////////////////////////////////////////////////////

package com.cisco.omdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cisco.omdb.Main;

/**
      * A OMDBUtil class is used for holding static methods for web URL request/response
      *
      * @author  Joe Horvath (joshorva@cisco.com)
      */

public class OMDBUtil extends Object
{
    private final static String CLASS_NAME = OMDBUtil.class.getName ();

    // log4j logger object
    private final static Logger logger     = LogManager.getLogger (OMDBUtil.CLASS_NAME);

    private static HeadersContentsResponse coreCommonWithoutPayload (final HttpUriRequest request) throws ClientProtocolException, IOException, UnsupportedOperationException
    {
        final CloseableHttpClient client = HttpClientBuilder.create ().build ();
        request.setHeader ("Content-type", "application/json");

        try
        {
            final HttpResponse response = client.execute (request);

            OMDBUtil.logger.debug ("HTTP Status Line: {}", response.getStatusLine ());

            final Map < String, String > headers = OMDBUtil.dumpHeaders (response);
            final JsonStructure contents = OMDBUtil.dumpContents (response);

            client.close ();

            final HeadersContentsResponse result = new HeadersContentsResponse (headers, contents, response);

            return (result);
        }
        finally
        {
            try
            {
                client.close ();
            }
            catch (final IOException e)
            {
                OMDBUtil.logger.warn ("Cannot close HTTP I/O Connection", e);
            }
        }
    }

    public static HeadersContentsResponse coreGet (final URI uri) throws ClientProtocolException, IOException, UnsupportedOperationException
    {
        OMDBUtil.logger.debug ("GET URL: \"{}\"", uri);

        final HttpGet get = new HttpGet (uri);
        return (OMDBUtil.coreCommonWithoutPayload (get));
    }

    public static JsonStructure dumpContents (final HttpResponse response) throws UnsupportedOperationException, IOException
    {
        final HttpEntity entity = response.getEntity ();

        if (null == entity)
            return (null);

        final InputStream input = entity.getContent ();
        if (0 == input.available ())
            return (null);

        final JsonStructure jsonStructure = Json.createReader (new InputStreamReader (input)).read ();

        OMDBUtil.logger.debug ("JSON response: {}", jsonStructure.toString ());

        return (jsonStructure);
    }

    public static Map < String, String > dumpHeaders (final HttpResponse response)
    {
        final Map < String, String > result = new HashMap <> ();

        for (final Header header : response.getAllHeaders ())
        {
            final String name = header.getName ();
            final String value = header.getValue ();

            OMDBUtil.logger.debug ("Header name: {}, value: {}", name, value);

            result.put (name, value);
        }

        return (result);
    }

    public static String getValueIfKeyPresent (final String key, final JsonObject jsonObject)
    {
        if (jsonObject.containsKey (key))
            return (jsonObject.getString (key));

        return (null);
    }

    public static JsonStructure wrappedCoreGet (final URI uri) throws ClientProtocolException, IOException, UnsupportedOperationException, LogicException
    {
        final HeadersContentsResponse response = OMDBUtil.coreGet (uri);
        final HttpResponse resp = response.getResponse ();
        final StatusLine sl = resp.getStatusLine ();
        final int code = sl.getStatusCode ();
        if (HttpStatus.SC_OK != code)
        {
            final String reason = sl.getReasonPhrase ();
            final JsonStructure js = response.getContents ();
            if (null != js)
            {
                final JsonObject jo = (JsonObject) js;
                final String message = OMDBUtil.getValueIfKeyPresent (Main.ERROR_KEY, jo);
                throw new LogicException ("Cannot perform GET (" + uri + ") -- code: " + code + ", reason: " + reason + ", message: " + message);
            }

            throw new LogicException ("Cannot perform GET (" + uri + ") -- code: " + code + ", reason: " + reason);
        }

        return (response.getContents ());
    }

    /**
     * Private constructor for OMDBUtil class
     *  Makes no sense to instantiate this object.
     *
     */
    private OMDBUtil ()
    {
        super ();
    }

    /**
     * Conversion to String for OMDBUtil class
     *
     * @return - A formatted String describing class OMDBUtil
     */
    @Override
    public String toString ()
    {
        return (super.toString () + "[" + OMDBUtil.CLASS_NAME + ": " +
                "]");
    }
}
