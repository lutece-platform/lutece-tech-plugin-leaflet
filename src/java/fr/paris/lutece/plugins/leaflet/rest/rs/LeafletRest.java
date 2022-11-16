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
package fr.paris.lutece.plugins.leaflet.rest.rs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import fr.paris.lutece.plugins.leaflet.rest.service.IPopupContentProvider;
import fr.paris.lutece.plugins.leaflet.service.CorsUtils;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * LeafletRest
 */
@Path( RestConstants.BASE_PATH + "leaflet" )
public class LeafletRest
{
    private static final String BEAN_PREFIX = "leaflet-rest-popup-provider-";
    private static final String PARAMETER_PROVIDER = "provider";
    private static final String PARAMETER_ID_DOCUMENT = "id_document";
    private static final String PARAMETER_CODE = "code";
    private static final String PATH_POPUP = "popup/{" + PARAMETER_PROVIDER + "}/{" + PARAMETER_ID_DOCUMENT + "}/{" +
        PARAMETER_CODE + "}";

    private static final String PROPERTY_CORS_ENABLED_KEY = "leaflet.cors.enabled";
    private static final boolean PROPERTY_CORS_ENABLED_DEFAULT = false;

  
    private static final String HEADER_ORIGIN = "origin";

    private static final String PROPERTY_CORS_METHODS_KEY = "leaflet.cors.methods";
    private static final String PROPERTY_CORS_METHODS_DEFAULT =  "GET, POST, DELETE, PUT";

    /**
     * Get an html snippet representing a leaflet popup
     * @param request the httpServletRequest
     * @param strProvider the provider
     * @param strIdDocument the id document
     * @param strCode the code in this document
     * @return the html of document
     */
    @GET
    @Path( PATH_POPUP )
    public Response getDocument( @Context
    HttpServletRequest request, @PathParam( PARAMETER_PROVIDER )
    String strProvider, @PathParam( PARAMETER_ID_DOCUMENT )
    String strIdDocument, @PathParam( PARAMETER_CODE )
    String strCode )
    {
        try
        {
            IPopupContentProvider popupContentProvider = (IPopupContentProvider) SpringContextService.getBean( BEAN_PREFIX +
                    strProvider );

            String popup = popupContentProvider.getPopup( request, strIdDocument, strCode );

            if ( popup != null )
            {
                String popupLocalized = I18nService.localize( popup, request.getLocale(  ) );

                ResponseBuilder responseBuilder = Response.ok();
                if ( AppPropertiesService.getPropertyBoolean( PROPERTY_CORS_ENABLED_KEY, PROPERTY_CORS_ENABLED_DEFAULT ) ) {
                    
                	String strHeaderOrigin = request.getHeader( HEADER_ORIGIN );
                	String strAccessControlAllowOrigin = CorsUtils.isValidOrigin( strHeaderOrigin ) ? strHeaderOrigin : "";
                	String strCorsMethods = AppPropertiesService.getProperty( PROPERTY_CORS_METHODS_KEY, PROPERTY_CORS_METHODS_DEFAULT);
                    responseBuilder .header("Access-Control-Allow-Methods", strCorsMethods).header("Access-Control-Allow-Origin", strAccessControlAllowOrigin);
                    
                }
                return responseBuilder
                    .entity(popupLocalized)
                    .build();
            }

            AppLogService.debug( "Leaflet popup rest API: icon was null" );
            throw new WebApplicationException( 404 );
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            AppLogService.error( "Leaflet popup rest API: Missing strProvider " + strProvider + " , strDocId " +
                strIdDocument + ", exception " + e );
            throw new WebApplicationException( 404 );
        }
    }
   
}
