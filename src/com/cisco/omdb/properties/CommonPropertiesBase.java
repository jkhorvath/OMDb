// /////////////////////////////////////////////////////////////////////
//
// Copyright (c) Cisco Systems, Inc. 2012 All rights reserved
// .:|:.:|:.
//
// /////////////////////////////////////////////////////////////////////
// File : CommonPropertiesBase.java
//
// Notes :
//
// Updates :
// new - Joe Horvath - December 13, 2018
// /////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class CommonPropertiesBase
//
// /////////////////////////////////////////////////////////////////////

package com.cisco.omdb.properties;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cisco.omdb.util.LogicException;

/**
 * A CommonPropertiesBase class is used for managing Java Property keys and values
 *
 * @author Joe Horvath (joshorva@cisco.com)
 */
public abstract class CommonPropertiesBase extends Properties implements CommonProperties_IF
{
    public static String        hostname         = null;

    private final static Logger logger           = LogManager.getLogger (CommonPropertiesBase.class.getName ());

    private final static long   serialVersionUID = 50916122L;

    static
    {
        try
        {
            CommonPropertiesBase.hostname = InetAddress.getLocalHost ().getHostName ();
        }
        catch (final UnknownHostException e)
        {
            CommonPropertiesBase.logger.error ("Cannot determine hostname", e);
        }
    }

    public static String getHostname ()
    {
        return (CommonPropertiesBase.hostname);
    }

    public static void sortProperties (final Properties properties)
    {
        if (CommonPropertiesBase.logger.isInfoEnabled ())
            try
            {
                // Output the sorted properties
                final StringWriter sw = new StringWriter ();
                properties.store (sw, " Sorted properties from class: " + properties.getClass ().getName () + " on " + new Date ());
                final BufferedReader br = new BufferedReader (new StringReader (sw.toString ()));
                final List < String > list = new ArrayList <> ();
                String line;
                while (null != (line = br.readLine ()))
                    list.add (line);
                Collections.sort (list);
                System.out.println ("#");
                System.out.println ("# Begin properties load from class: " + properties.getClass ().getName ());
                System.out.println ("#");
                for (final String record : list)
                    System.out.println (record);
                System.out.println ("#");
                System.out.println ("# End properties from class: " + properties.getClass ().getName ());
                System.out.println ("#");
            }
            catch (final IOException e)
            {
                CommonPropertiesBase.logger.warn ("Cannot sort properties", e);
            }
    }

    protected CommonPropertiesBase ()
    {
        loadProperties ();
    }

    protected String getPropertyScoped (final String property)
    {
        // Try to scope to host name
        String result = getProperty (property + "." + CommonPropertiesBase.getHostname ());

        if (null != result)
        {
            CommonPropertiesBase.logger.debug ("Getting scoped property ({}): {}", property, result);
            return (result);
        }

        CommonPropertiesBase.logger.debug ("No scoped property value for: {}", property);

        result = getProperty (property);

        CommonPropertiesBase.logger.debug ("Getting non-scoped property ({}): {}", property, result);

        return (result);
    }

    protected String getStringProperty (final String property)
    {
        String result = getPropertyScoped (property);

        if (null == result)
            CommonPropertiesBase.logger.warn ("No property specified for: {}", property);
        else
            result = result.trim ();

        return (result);
    }

    protected String getStringProperty (final String property, final String defaultValue)
    {
        String result = getPropertyScoped (property);

        if (null == result)
        {
            CommonPropertiesBase.logger.warn ("No property specified for: {}, using default value: {}", property, defaultValue);
            result = defaultValue;
        }
        else
            result = result.trim ();

        return (result);
    }

    protected void loadProperties ()
    {
        final String propertiesFileName = getPropertiesFileName ();

        CommonPropertiesBase.logger.info ("Loading properties from file: {}", propertiesFileName);

        // Then load module properties
        InputStream is = null;
        Reader reader = null;
        try
        {
            //            load (instance.getClass ().getClassLoader ().getResourceAsStream (propertiesFileName));
            final URL resourceUrl = getClass ().getClassLoader ().getResource (propertiesFileName);

            if (null == resourceUrl)
                throw new LogicException ("Cannot find property file: " + propertiesFileName);

            CommonPropertiesBase.logger.info ("Module Resource file URL: {}", resourceUrl);

            is = getClass ().getClassLoader ().getResourceAsStream (propertiesFileName);
            // Support native 16 bit Unicode in property files
            reader = new InputStreamReader (is, "UTF-8");
            load (reader);
        }
        catch (final Exception e)
        {
            CommonPropertiesBase.logger.error ("Cannot load properties ({}) from resource as stream: {}", propertiesFileName, e);

            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream (propertiesFileName);
                load (fis);
                CommonPropertiesBase.logger.warn ("Read properties directly from file: {}", propertiesFileName);
            }
            catch (final IOException e1)
            {
                CommonPropertiesBase.logger.error ("Cannot load properties from file ({}): {}", propertiesFileName, e1);
            }
            finally
            {
                if (null != fis)
                    try
                    {
                        fis.close ();
                    }
                    catch (final IOException e1)
                    {
                        CommonPropertiesBase.logger.warn ("Cannot close properties resource input stream from file {}: {}", propertiesFileName, e1);
                    }
            }
        }
        finally
        {
            if (null != reader)
                try
                {
                    reader.close ();
                }
                catch (final IOException e)
                {
                    CommonPropertiesBase.logger.warn ("Cannot close properties resource input stream from file {}: {}", propertiesFileName, e);
                }
            if (null != is)
                try
                {
                    is.close ();
                }
                catch (final IOException e)
                {
                    CommonPropertiesBase.logger.warn ("Cannot close properties resource input stream from file {}: {}", propertiesFileName, e);
                }

            CommonPropertiesBase.sortProperties (this);
        }
    }
}
