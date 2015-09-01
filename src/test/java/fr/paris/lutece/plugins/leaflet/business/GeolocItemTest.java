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

import junit.framework.TestCase;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;


public class GeolocItemTest extends TestCase
{
    public void testSerialize(  ) throws JsonParseException, JsonMappingException, IOException
    {
        GeolocItem ref = new GeolocItem(  );
        HashMap<String, Object> pref = new HashMap<String, Object>(  );
        pref.put( "layer", "aco" );
        pref.put( "icon", "ico" );
        pref.put( "address", "addo" );

        HashMap<String, Object> gref = new HashMap<String, Object>(  );
        gref.put( "coordinates", Arrays.asList( new Double[] { 2.31272, 48.83632 } ) );
        ref.setGeometry( gref );
        ref.setProperties( pref );

        String strRefJSON = ref.toJSON(  );
        ObjectMapper objectMapper = new ObjectMapper(  );
        JsonNode a = objectMapper.readTree( strRefJSON );

        String s = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.31272,48.83632]},\"properties\":{\"layer\":\"aco\", \"icon\":\"ico\", \"address\":\"addo\"}}";
        JsonNode b = objectMapper.readTree( s );
        assert a.equals( b ) : "Parsing ref string and ref.toJSON should produce equal objects";

        GeolocItem g = GeolocItem.fromJSON( s );
        assert g.getLonLat(  ).equals( ref.getLonLat(  ) ) : "Check serialized field";
        assert g.getAddress(  ).equals( ref.getAddress(  ) ) : "Check serialized field";
        assert g.getIcon(  ).equals( ref.getIcon(  ) ) : "Check serialized field";
        assert g.getLayer(  ).equals( ref.getLayer(  ) ) : "Check serialized field";

        GeolocItem g2 = GeolocItem.fromJSON( g.toJSON(  ) );
        assert g2.getLonLat(  ).equals( ref.getLonLat(  ) ) : "Check serialized field";
        assert g2.getAddress(  ).equals( ref.getAddress(  ) ) : "Check serialized field";
        assert g2.getIcon(  ).equals( ref.getIcon(  ) ) : "Check serialized field";
        assert g2.getLayer(  ).equals( ref.getLayer(  ) ) : "Check serialized field";

        String strRefXML = "<geoloc>\r\n" + "<lon>2.31272</lon>\r\n" + "<lat>48.83632</lat>\r\n" +
            "<address><![CDATA[addo]]></address>\r\n" + "<icon>ico</icon>\r\n" + "<layer>aco</layer>\r\n" +
            "</geoloc>\r\n";

        String strXML = ref.toXML(  );
        assert strRefXML.equals( strXML ) : "Test xml marshalling: ref:\n" + strRefXML + "\n; got:\n" + strXML;

        GeolocItem missingref = new GeolocItem(  );
        HashMap<String, Object> missingpref = new HashMap<String, Object>(  );
        HashMap<String, Object> missinggref = new HashMap<String, Object>(  );
        missinggref.put( "coordinates", Arrays.asList( new Double[] { 2.31272, 48.83632 } ) );
        missingref.setGeometry( missinggref );
        missingref.setProperties( missingpref );

        String strMissingRefXML = "<geoloc>\r\n" + "<lon>2.31272</lon>\r\n" + "<lat>48.83632</lat>\r\n" +
            "</geoloc>\r\n";

        String strMissingXML = missingref.toXML(  );
        assert strMissingRefXML.equals( strMissingXML ) : "Test missing xml marshalling: ref:\n" + strRefXML +
        "\n; got:\n" + strXML;

        String smissing = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.31272,48.83632]},\"properties\":{}}";
        JsonNode bmissing = objectMapper.readTree( smissing );
        String jsonmissing = missingref.toJSON(  );
        JsonNode bmissingref = objectMapper.readTree( jsonmissing );
        assert bmissingref.equals( bmissing ) : "Parsing,for missing fields, ref string and ref.toJSON should produce equal objects. strings are: \n" +
        smissing + "\n and \n" + jsonmissing;
    }
}
