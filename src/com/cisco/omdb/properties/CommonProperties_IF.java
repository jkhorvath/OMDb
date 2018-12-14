// /////////////////////////////////////////////////////////////////////
//
// Copyright (c) Cisco Systems, Inc. 2012 All rights reserved
// .:|:.:|:.
//
// /////////////////////////////////////////////////////////////////////
// File : CommonProperties_IF.java
//
// Notes :
//
// Updates :
// new - Joe Horvath - December 13, 2018
// /////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the interface CommonProperties_IF
//
// /////////////////////////////////////////////////////////////////////

package com.cisco.omdb.properties;

public interface CommonProperties_IF
{
    public final static String LOGGER_PROPERTIES_FILENAME = "log4j2.xml";

    public String getPropertiesFileName ();
}
