// /////////////////////////////////////////////////////////////////////
//

// Copyright (c) Cisco Systems, Inc. 2014 All rights reserved
// .:|:.:|:.
//
// /////////////////////////////////////////////////////////////////////
// File : OMDBProperties.java
//
// Notes :
//
// Updates :
// new - Joe Horvath - July 29, 2014
// /////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class OMDBProperties
//
// /////////////////////////////////////////////////////////////////////

package com.cisco.omdb.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A OMDBProperties class is used for...
 *
 * @version %I%
 * @author Joe Horvath (joshorva@cisco.com)
 */

public class OMDBProperties extends CommonPropertiesBase
{
    private static String               apiKey;

    private final static OMDBProperties instance;

    // log4j logger object
    private final static Logger         logger                = LogManager.getLogger (OMDBProperties.class.toString ());

    private final static String         PROPERTIES_FILE_NAME  = "omdb.properties";

    private final static String         PROPERTY_OMDB_API_KEY = "com.cisco.omdb.key.api";

    private final static long           serialVersionUID      = 5091612332L;

    static
    {
        instance = new OMDBProperties ();
        OMDBProperties.instance.rebuildPropertyValues ();
    }

    /**
     * Getter for OMDBProperties.java variable apiKey
     *  Returns the current value of OMDBProperties.java's class variable apiKey
     *
     * @return - The current value for OMDBProperties.java's class variable apiKey as a(n) static String
     */
    public static String getApiKey ()
    {
        return (OMDBProperties.apiKey);
    }

    /**
     * Public constructor for OMDBProperties class
     *
     */
    public OMDBProperties ()
    {
        super ();
    }

    @Override
    public String getPropertiesFileName ()
    {
        return (OMDBProperties.PROPERTIES_FILE_NAME);
    }

    private void rebuildPropertyValues ()
    {
        OMDBProperties.apiKey = getStringProperty (OMDBProperties.PROPERTY_OMDB_API_KEY);
    }

    /**
     * Conversion to String for OMDBProperties class
     *
     * @return - A formatted String describing class OMDBProperties
     */
    @Override
    public synchronized String toString ()
    {
        return (super.toString () + "[" + getClass ().getName () + ": " +
                "static String apiKey = '" + OMDBProperties.apiKey + "', " +
                "static String PROPERTIES_FILE_NAME = '" + OMDBProperties.PROPERTIES_FILE_NAME + "', " +
                "]");
    }
}
