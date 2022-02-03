/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.leaflet.business;

import fr.paris.lutece.util.xml.XmlUtil;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A geoloc item with methods to translate to/from geojson and to XML
 */
@JsonAutoDetect( creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE )
public class GeolocItem
{
    public static final String PATH_TYPE = "type";
    public static final String PATH_GEOMETRY = "geometry";
    public static final String PATH_GEOMETRY_TYPE = "type";
    public static final String PATH_GEOMETRY_COORDINATES = "coordinates";
    public static final String PATH_PROPERTIES = "properties";
    public static final String PATH_PROPERTIES_ADDRESS = "address";
    public static final String PATH_PROPERTIES_ICON = "icon";
    public static final String PATH_PROPERTIES_LAYER = "layer";
    public static final String XML_ROOT = "geoloc";
    public static final String XML_LON = "lon";
    public static final String XML_LAT = "lat";
    public static final String XML_ADDRESS = "address";
    public static final String XML_ICON = "icon";
    public static final String XML_LAYER = "layer";
    public static final String VALUE_TYPE = "Feature";
    public static final String VALUE_GEOMETRY_TYPE = "Point";
    private static final ObjectMapper _objectMapper;

    static
    {
        _objectMapper = new ObjectMapper(  );
        _objectMapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    private List<Double> _lonlat;
    private String _address;
    private String _icon;
    private String _layer;

    //Example
    //{"type":"Feature","geometry":{"type":"Point","coordinates":[2.31272,48.83632]},"properties":{"layer":"aco"}}

    /**
     * Sets the Geometry
     *
     * @param geometry the geometry
     */
    @JsonProperty( PATH_GEOMETRY )
    public void setGeometry( Map<String, Object> geometry )
    {
        _lonlat = ( List<Double> ) geometry.get( PATH_GEOMETRY_COORDINATES );
    }

    /**
     * Sets the Properties
     *
     * @param properties the geometry
     */
    @JsonProperty( PATH_PROPERTIES )
    public void setProperties( Map<String, Object> properties )
    {
        _address = (String) properties.get( PATH_PROPERTIES_ADDRESS );
        _icon = (String) properties.get( PATH_PROPERTIES_ICON );
        _layer = (String) properties.get( PATH_PROPERTIES_LAYER );
    }

    /**
     * Returns the Type
     *
     * @return The type
     */
    @JsonProperty( PATH_TYPE )
    public String getType(  )
    {
        return VALUE_TYPE;
    }

    /**
     * Returns the Properties
     *
     * @return The properties
     */
    @JsonProperty( PATH_PROPERTIES )
    public Map<String, Object> getProperties(  )
    {
        HashMap<String, Object> properties = new HashMap<>(  );

        if ( _address != null )
        {
            properties.put( PATH_PROPERTIES_ADDRESS, _address );
        }

        if ( _icon != null )
        {
            properties.put( PATH_PROPERTIES_ICON, _icon );
        }

        if ( _layer != null )
        {
            properties.put( PATH_PROPERTIES_LAYER, _layer );
        }

        return properties;
    }

    /**
     * Returns the Geometry
     *
     * @return The geometry
     */
    @JsonProperty( PATH_GEOMETRY )
    public Map<String, Object> getGeometry(  )
    {
        HashMap<String, Object> geometry = new HashMap<>(  );
        geometry.put( PATH_GEOMETRY_TYPE, VALUE_GEOMETRY_TYPE );
        geometry.put( PATH_GEOMETRY_COORDINATES, _lonlat );

        return geometry;
    }

    /**
     * Returns the LonLat
     *
     * @return The LonLat
     */
    public List<Double> getLonLat(  )
    {
        return _lonlat;
    }

    /**
     * Returns the Lon
     *
     * @return The Lon
     */
    public double getLon(  )
    {
        return _lonlat.get( 0 );
    }

    /**
     * Returns the Lat
     *
     * @return The Lat
     */
    public double getLat(  )
    {
        return _lonlat.get( 1 );
    }

    /**
     * Returns the Address
     *
     * @return The address
     */
    public String getAddress(  )
    {
        return _address;
    }

    /**
     * Returns the Icon
     *
     * @return The icon
     */
    public String getIcon(  )
    {
        return _icon;
    }

    /**
     * Returns the Layer
     *
     * @return The layer
     */
    public String getLayer(  )
    {
        return _layer;
    }

    /**
     * Sets the Icon
     *
     * @param icon the icon
     */
    public void setIcon( String icon )
    {
        _icon = icon;
    }

    /**
     * Writes the geojson to a String
     *
     * @return The geojseon String
     */
    public String toJSON(  )
    {
        try
        {
            return _objectMapper.writeValueAsString( this );
        }
        catch ( IOException e )
        {
            // Writing to strings should not produce exceptions
            throw new RuntimeException( e );
        }
    }

    /**
     * Writes the xml to a String. This is a non standard representation.
     *
     * @return The xml String
     */
    public String toXML(  )
    {
        StringBuffer stringBuffer = new StringBuffer(  );
        XmlUtil.beginElement( stringBuffer, XML_ROOT );
        XmlUtil.addElement( stringBuffer, XML_LON, _lonlat.get( 0 ).toString(  ) );
        XmlUtil.addElement( stringBuffer, XML_LAT, _lonlat.get( 1 ).toString(  ) );

        if ( _address != null )
        {
            XmlUtil.addElementHtml( stringBuffer, XML_ADDRESS, _address );
        }

        if ( _icon != null )
        {
            XmlUtil.addElement( stringBuffer, XML_ICON, _icon );
        }

        if ( _layer != null )
        {
            XmlUtil.addElement( stringBuffer, XML_LAYER, _layer );
        }

        XmlUtil.endElement( stringBuffer, XML_ROOT );

        return stringBuffer.toString(  );
    }

    /**
     * Parses a geojson string to build a GeolocItem Object.
     *
     * @return The geolocItem object
     */
    public static GeolocItem fromJSON( String strJson )
        throws IOException
    {
        return _objectMapper.readValue( strJson, GeolocItem.class );
    }
}
