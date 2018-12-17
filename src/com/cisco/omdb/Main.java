///////////////////////////////////////////////////////////////////////
//
//     Copyright (c) Cisco Systems, Inc. 2018 All rights reserved
//                     .:|:.:|:.
//
///////////////////////////////////////////////////////////////////////
// File                 :  Main.java
//
// Notes                :
//
// Updates              :
//                               new - Joe Horvath - December 13, 2018
///////////////////////////////////////////////////////////////////////
//
// *********************** Module/Class Description *******************
//
// This file contains the definition of the class Main
//
///////////////////////////////////////////////////////////////////////

package com.cisco.omdb;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cisco.omdb.properties.OMDBProperties;
import com.cisco.omdb.util.LogicException;
import com.cisco.omdb.util.OMDBUtil;

/**
 * A Main class is used for illustrating an example request/response to The Open Movie Database
 *
 * @author  Joe Horvath (joshorva@cisco.com)
 */

public class Main extends Object
{
    private final static String API_KEY_PARAM               = "apikey";

    private final static String CLASS_NAME                  = Main.class.getName ();

    private final static String DESIRED_RATING_SOURCE       = "Rotten Tomatoes";
    public final static String  ERROR_KEY                   = "Error";

    // log4j logger object
    private final static Logger logger                      = LogManager.getLogger (Main.CLASS_NAME);

    private final static String MOVIE_TITLE_PARAM           = "t";
    private final static String MOVIE_TITLE_VALUE_KEY       = "Title";
    private final static String MOVIE_YEAR_PARAM            = "y";
    private final static String OMDB_API_URL                = "http://www.omdbapi.com";
    private final static String OMDB_MOVIE_PLOT_TYPE        = "plot";
    private final static String OMDB_MOVIE_SHORT_PLOT_VALUE = "short";
    private final static String PLOT_OPTION                 = "-p";
    private final static String RATING_SOURCE_KEY           = "Source";
    private final static String RATING_VALUE_KEY            = "Value";
    private final static String RATINGS_KEY                 = "Ratings";
    private final static String RESPONSE_DATA_TYPE_PARAM    = "r";
    private final static String RESPONSE_JSON_TYPE          = "json";
    private final static String RESPONSE_TYPE_KEY           = "Response";
    private final static String TYPE_OPTION                 = "-t";
    private final static String TYPE_PARAM                  = "type";

    public static void main (final String [] args)
    {
        // Prepare to scan through command line arguments to look for input parameters and options.
        String plotRequested = Main.OMDB_MOVIE_SHORT_PLOT_VALUE;
        String typeRequested = null;
        String movieNameRequested = null;
        String movieYearRequested = null;

        int argsIndex = 0;

        while (argsIndex < args.length)
            if (Main.PLOT_OPTION.equals (args[argsIndex]))
            {
                ++argsIndex;

                if (args.length <= argsIndex)
                    Main.usage ();

                plotRequested = args[argsIndex++];

                Main.logger.debug ("Using \"plot\" argument: \"{}\"", plotRequested);
            }
            else if (Main.TYPE_OPTION.equals (args[argsIndex]))
            {
                ++argsIndex;

                if (args.length <= argsIndex)
                    Main.usage ();

                typeRequested = args[argsIndex++];
                Main.logger.debug ("Using \"type\" argument: \"{}\"", typeRequested);
            }
            else if (null == movieNameRequested)
            {
                // First non-option is the movie name
                movieNameRequested = args[argsIndex++];
                Main.logger.debug ("Using \"movie name\" argument: \"{}\"", movieNameRequested);
            }
            else
            {
                // If there is another non-option, then that is movie year.
                movieYearRequested = args[argsIndex++];
                Main.logger.debug ("Using \"movie year\" argument: \"{}\"", movieYearRequested);
            }

        // If we didn't get at least a movie name, then we cannot proceed
        if (null == movieNameRequested)
            Main.usage ();

        try
        {
            // Build up the HTTP GET URL and URI to the OMDb database service
            final URIBuilder builder = new URIBuilder (Main.OMDB_API_URL);
            builder.addParameter (Main.MOVIE_TITLE_PARAM, movieNameRequested);
            builder.addParameter (Main.API_KEY_PARAM, OMDBProperties.getApiKey ());

            if (null != typeRequested)
                builder.addParameter (Main.TYPE_PARAM, typeRequested);

            builder.addParameter (Main.RESPONSE_DATA_TYPE_PARAM, Main.RESPONSE_JSON_TYPE);
            builder.addParameter (Main.OMDB_MOVIE_PLOT_TYPE, plotRequested);

            if (null != movieYearRequested)
                builder.addParameter (Main.MOVIE_YEAR_PARAM, movieYearRequested);

            // Perform the HTTP GET to the OMDb service
            final JsonStructure jsonResponse = OMDBUtil.wrappedCoreGet (builder.build ());

            // Make sure that we can handle the response type
            if (JsonValue.ValueType.OBJECT == jsonResponse.getValueType ())
            {
                // Move the response into an object that we can inspect
                final JsonObject movieJsonObject = (JsonObject) jsonResponse;

                // Did OMDb locate the movie?
                final String responseType = OMDBUtil.getValueIfKeyPresent (Main.RESPONSE_TYPE_KEY, movieJsonObject);

                if (Boolean.parseBoolean (responseType))
                {
                    // OMDb did find the movie
                    final String movieTitle = movieJsonObject.getString (Main.MOVIE_TITLE_VALUE_KEY);

                    // Pick up all the attributes that OMDb gave us and output them to the log
                    final StringBuffer label = new StringBuffer ();

                    for (final Object keyObj : movieJsonObject.keySet ())
                    {
                        final String key = (String) keyObj;
                        final Object valueObj = movieJsonObject.get (keyObj);

                        if (valueObj instanceof JsonString)
                        {
                            final String value = valueObj.toString ();

                            Main.logger.debug ("Key: \"{}\", value: {}", key, value);

                            if (label.length () > 0)
                                label.append (", ");
                            label.append (key + ": " + value);
                        }
                    }

                    Main.logger.info ("Located {}", label.toString ());

                    // Scan through all the ratings (if any) looking for the "Rotten Tomatoes" rating
                    final JsonArray ratings = movieJsonObject.getJsonArray (Main.RATINGS_KEY);

                    boolean found = false;

                    for (final JsonObject rating : ratings.getValuesAs (JsonObject.class))
                    {
                        final String ratingSource = OMDBUtil.getValueIfKeyPresent (Main.RATING_SOURCE_KEY, rating);

                        final String movieRating = OMDBUtil.getValueIfKeyPresent (Main.RATING_VALUE_KEY, rating);

                        if (Main.DESIRED_RATING_SOURCE.equals (ratingSource))
                        {
                            // We found a rating from "Rotten Tomatoes" (there may be more?)
                            found = true;

                            Main.logger.info ("Movie title \"{}\" has rating of {} from \"{}\"", movieTitle, movieRating, ratingSource);
                            System.out.println ("Movie title \"" + movieTitle + "\" has rating of " + movieRating + " from \"" + ratingSource + "\"");
                        }
                        else
                            Main.logger.info ("Movie rating source ({}) of \"{}\" for Movie Title: {} is not the desired rating source ({})", ratingSource, movieRating, movieTitle, Main.DESIRED_RATING_SOURCE);
                    }

                    // Check if we never found a rating from "Rotten Tomatoes"
                    if (!found)
                        Main.logger.warn ("Could not find desired rating source ({}) for Movie Title: {}", Main.DESIRED_RATING_SOURCE, movieTitle);
                }
                else
                {
                    // OMDb could not find the movie
                    final String errorMessage = OMDBUtil.getValueIfKeyPresent (Main.ERROR_KEY, movieJsonObject);
                    Main.logger.warn ("Could not retrieve movie \"{}\"{}: {}", movieNameRequested, ((null == movieYearRequested) ? "" : " for year " + movieYearRequested), errorMessage);
                }
            }
            else
                Main.logger.error ("Unhandled data type ({}) returned -- Not a JSON Object", jsonResponse.getValueType ());
        }
        catch (final URISyntaxException e)
        {
            Main.logger.error ("Cannot build URI for OMDb: {}", e);
        }
        catch (IOException | LogicException | UnsupportedOperationException e)
        {
            Main.logger.error ("Cannot perform request against OMDb: {}", e);
        }
    }

    private static void usage ()
    {
        Main.logger.error ("usage: java {} [-p short|full] [-t movie|series|episode] <movie-name> [movie-year]", Main.CLASS_NAME);
        System.exit (1);
    }

    /**
     * Private constructor for Main class
     *  Makes no sense to instantiate this object.
     *
     */
    private Main ()
    {
        super ();
    }

    /**
     * Conversion to String for Main class
     *
     * @return - A formatted String describing class Main
     */
    @Override
    public synchronized String toString ()
    {
        return (super.toString () + "[" + Main.CLASS_NAME + ": " +
                "]");
    }
}
