/*
 * Copyright (c) 2002-2022, Mairie de Paris
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
package fr.paris.lutece.plugins.leaflet.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the embedded Leaflet library renders the map tiles as decorative images
 */
public class LeafletLibraryTileTest
{
    private static final Path LEAFLET_DIR = Paths.get( "webapp/js/plugins/leaflet/leaflet" );

    /**
     * The tile factory of the source library sets an empty alt and the presentation role
     *
     * @throws IOException
     *             if the library cannot be read
     */
    @Test
    void testSourceTilesAreDecorative( ) throws IOException
    {
        String strTileFactory = tileFactory( "leaflet-src.js", "var tile = L.DomUtil.create('img', 'leaflet-tile');" );

        assertTrue( strTileFactory.contains( "tile.alt = '';" ), strTileFactory );
        assertTrue( strTileFactory.contains( "tile.setAttribute('role', 'presentation');" ), strTileFactory );
    }

    /**
     * The tile factory of the minified library sets an empty alt and the presentation role
     *
     * @throws IOException
     *             if the library cannot be read
     */
    @Test
    void testMinifiedTilesAreDecorative( ) throws IOException
    {
        String strTileFactory = tileFactory( "leaflet.js", "var t=o.DomUtil.create(\"img\",\"leaflet-tile\");" );

        assertTrue( strTileFactory.contains( "t.alt=\"\"" ), strTileFactory );
        assertTrue( strTileFactory.contains( "t.setAttribute(\"role\",\"presentation\")" ), strTileFactory );
    }

    private static String tileFactory( String strFile, String strStart ) throws IOException
    {
        String strLibrary = new String( Files.readAllBytes( LEAFLET_DIR.resolve( strFile ) ), StandardCharsets.UTF_8 );
        int nStart = strLibrary.indexOf( strStart );
        assertTrue( nStart >= 0, "Image tile factory not found in " + strFile );

        return strLibrary.substring( nStart, Math.min( strLibrary.length( ), nStart + 400 ) );
    }
}
