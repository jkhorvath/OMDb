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

package com.cisco.omdb;

import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cisco.as.base.properties.CommonPropertiesBase;

/**
 * A OMDBProperties class is used for...
 *
 * @version %I%
 * @author Joe Horvath (joshorva@cisco.com)
 */

public class OMDBProperties extends CommonPropertiesBase
{
    private static String       apiKey;

    // log4j logger object
    private final static Logger logger                = LogManager.getLogger (OMDBProperties.class.toString ());

    private final static String PROPERTIES_FILE_NAME  = "omdb.properties";

    private final static String PROPERTY_OMDB_API_KEY = "com.cisco.omdb.key.api";

    private final static long   serialVersionUID      = 5091612332L;

    static
    {
        OMDBProperties.rebuildPropertyValues ();

        CommonPropertiesBase.addObserver (new Observer ()
        {
            @Override
            public void update (final Observable o, final Object arg)
            {
                OMDBProperties.logger.info ("Properties reloaded");
                OMDBProperties.rebuildPropertyValues ();
            }
        });
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

    public static synchronized OMDBProperties getInstance ()
    {
        return (OMDBProperties) (CommonPropertiesBase.getInstance (OMDBProperties.class));
    }

    private static void rebuildPropertyValues ()
    {
        final OMDBProperties properties = OMDBProperties.getInstance ();

        OMDBProperties.apiKey = properties.getStringProperty (OMDBProperties.PROPERTY_OMDB_API_KEY);
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
