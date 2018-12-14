// /////////////////////////////////////////////////////////////////////
//
// Copyright (c) Cisco Systems, Inc. 2015 All rights reserved
// .:|:.:|:.
//
// /////////////////////////////////////////////////////////////////////
// File : LogicException.java
//
// Notes :
//
// Updates :
// new - Joe Horvath - December 13, 2018
// /////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class LogicException
//
// /////////////////////////////////////////////////////////////////////

package com.cisco.omdb.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A LogicException class is used for signaling application exceptions
 *
 * @author Joe Horvath (joshorva@cisco.com)
 */

public class LogicException extends Exception
{
    // log4j logger object
    private final static Logger logger           = LogManager.getLogger (LogicException.class);

    private final static long   serialVersionUID = 1556763794L;

    /**
     * Public constructor for LogicException class
     *
     * @param message
     *            - A String representing ...
     */
    public LogicException (final String message)
    {
        super (message);

        LogicException.logger.warn (message);
    }
}
