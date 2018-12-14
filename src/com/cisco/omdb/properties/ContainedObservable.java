///////////////////////////////////////////////////////////////////////
//
//     Copyright (c) Cisco Systems, Inc. 2017 All rights reserved
//                     .:|:.:|:.
//
///////////////////////////////////////////////////////////////////////
// File                 :  ContainedObservable.java
//
// Notes                :
//
// Updates              :
//                               new - Superuser - April 24, 2017
///////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class ContainedObservable
//
///////////////////////////////////////////////////////////////////////

package com.cisco.omdb.properties;

import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
      * A ContainedObservable class is used for...
      *
      * @version %I%
      * @author Superuser (joshorva@cisco.com)
      */

public class ContainedObservable extends Observable
{
    private final static String CLASS_NAME = ContainedObservable.class.getName ();

    // log4j logger object
    @SuppressWarnings ("unused")
    private final static Logger logger     = LogManager.getLogger (ContainedObservable.CLASS_NAME);

    // public final static String VERSION_STRING = "Version of " + ContainedObservable.CLASS_NAME + " is '%I%'";

    /**
     * Public constructor for ContainedObservable class
     *
     */
    public ContainedObservable ()
    {
        super ();
    }

    public void delegatedClearChanged ()
    {
        clearChanged ();
    }

    public void delegatedSetChanged ()
    {
        setChanged ();
    }

    /**
     * Conversion to String for ContainedObservable class
     *
     * @return - A formatted String describing class ContainedObservable
     */
    @Override
    public String toString ()
    {
        return (super.toString () + "[" + ContainedObservable.CLASS_NAME + ": " +
                "]");
    }
}
