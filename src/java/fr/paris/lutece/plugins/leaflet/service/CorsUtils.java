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
package fr.paris.lutece.plugins.leaflet.service;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import fr.paris.lutece.portal.service.util.AppPropertiesService;



/*
 * CorsUtils
 */
public final class CorsUtils
{
    
    /** The Constant PATH_MATCHER. */
    private final static PathMatcher PATH_MATCHER = new AntPathMatcher( );
    
    /** The Constant PROPERTY_CORS_ORIGIN_KEY. */
    private static final String PROPERTY_CORS_ORIGIN_KEY = "leaflet.cors.origin"; 

    /**
     * Private constructor.
     */
    private CorsUtils( )
    {
    }

   
    
    /**
     * Checks if is valid origin.
     *
     * @param strOrigin the str origin
     * @return the boolean
     */
    public static Boolean isValidOrigin( String strOrigin )
    {
       
        String strAuthorizedOrigin = AppPropertiesService.getProperty( PROPERTY_CORS_ORIGIN_KEY );

        return isValidOrigin(strOrigin, strAuthorizedOrigin);
    }
    
    /**
     * Checks if is valid origin.
     *
     * @param strOrigin the str origin
     * @param strCorsOriginPatterns the cors origin pattern used to test
     * @return the boolean
     */
    public static Boolean isValidOrigin( String strOrigin,String strCorsOriginPatterns)
    {
       
        if ( !StringUtils.isEmpty( strCorsOriginPatterns ) )
        {
         
            String [ ] tabAuthorizedDomains = strCorsOriginPatterns.split( "," );

            for ( int i = 0; i < tabAuthorizedDomains.length; i++ )
            {
                if ( PATH_MATCHER.match( tabAuthorizedDomains [i], strOrigin ) )
                {
                   return true;
                }
               
            }
        }

        return false;
    }


}
