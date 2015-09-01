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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonAutoDetect( creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE )
public class GeolocItem
{
    private static final ObjectMapper _objectMapper;

    static
    {
        _objectMapper = new ObjectMapper(  );
        _objectMapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

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
    private List<Double> _lonlat;
    private String _address;
    private String _icon;
    private String _layer;

    //Example
    //{"type":"Feature","geometry":{"type":"Point","coordinates":[2.31272,48.83632]},"properties":{"layer":"aco"}}
    @JsonProperty( PATH_GEOMETRY )
    public void setGeometry( Map<String, Object> geometry )
    {
        _lonlat = (List<Double>) geometry.get( PATH_GEOMETRY_COORDINATES );
    }

    @JsonProperty( PATH_PROPERTIES )
    public void setProperties( Map<String, Object> properties )
    {
        _address = (String) properties.get( PATH_PROPERTIES_ADDRESS );
        _icon = (String) properties.get( PATH_PROPERTIES_ICON );
        _layer = (String) properties.get( PATH_PROPERTIES_LAYER );
    }

    @JsonProperty( PATH_TYPE )
    public String getType(  )
    {
        return VALUE_TYPE;
    }

    @JsonProperty( PATH_PROPERTIES )
    public Map<String, Object> getProperties(  )
    {
        HashMap<String, Object> properties = new HashMap<String, Object>(  );

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

    @JsonProperty( PATH_GEOMETRY )
    public Map<String, Object> getGeometry(  )
    {
        HashMap<String, Object> geometry = new HashMap<String, Object>(  );
        geometry.put( PATH_GEOMETRY_TYPE, VALUE_GEOMETRY_TYPE );
        geometry.put( PATH_GEOMETRY_COORDINATES, _lonlat );

        return geometry;
    }

    public List<Double> getLonLat(  )
    {
        return _lonlat;
    }

    public double getLon(  )
    {
        return _lonlat.get( 0 );
    }

    public double getLat(  )
    {
        return _lonlat.get( 1 );
    }

    public String getAddress(  )
    {
        return _address;
    }

    public String getIcon(  )
    {
        return _icon;
    }

    public String getLayer(  )
    {
        return _layer;
    }

    public void setIcon( String icon )
    {
        _icon = icon;
    }

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

    public String toXML(  )
    {
        StringBuffer sb = new StringBuffer(  );
        XmlUtil.beginElement( sb, XML_ROOT );
        XmlUtil.addElement( sb, XML_LON, _lonlat.get( 0 ).toString(  ) );
        XmlUtil.addElement( sb, XML_LAT, _lonlat.get( 1 ).toString(  ) );

        if ( _address != null )
        {
            XmlUtil.addElementHtml( sb, XML_ADDRESS, _address );
        }

        if ( _icon != null )
        {
            XmlUtil.addElement( sb, XML_ICON, _icon );
        }

        if ( _layer != null )
        {
            XmlUtil.addElement( sb, XML_LAYER, _layer );
        }

        XmlUtil.endElement( sb, XML_ROOT );

        return sb.toString(  );
    }

    public static GeolocItem fromJSON( String strJson )
        throws JsonParseException, JsonMappingException, IOException
    {
        return _objectMapper.readValue( strJson, GeolocItem.class );
    }
}
