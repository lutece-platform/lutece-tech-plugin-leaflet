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
package fr.paris.lutece.plugins.leaflet.service;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


/**
 * A service for icons
 */
public class IconService
{
    private static final String BEAN_PREFIX = "leaflet-icon-provider-";
    private static final String DATASTORE_PREFIX = "leaflet.icon.";
    private static final String DATASTORE_ICONS_PREFIX = DATASTORE_PREFIX + "icons.";


    /** Private constructor */
    private IconService(  )
    {
    }

    /**
     * Returns the list of installed icons.
     *
     * @return the list of icons
     */
    public static Collection<String> getList(  )
    {
        ReferenceList referenceList = DatastoreService.getDataByPrefix( DATASTORE_ICONS_PREFIX );
        HashSet<String> hashSet = new HashSet<>(  );

        for ( ReferenceItem referenceItem : referenceList )
        {
            String codeSuffix = referenceItem.getCode(  ).substring( DATASTORE_ICONS_PREFIX.length(  ) );
            String code = codeSuffix.substring( 0, codeSuffix.indexOf( '.' ) );
            hashSet.add( code );
        }

        return new ArrayList<>( hashSet );
    }

    /**
     * Returns the name of an icon based on the provider and the key.
     *
     * @param strProvider the provider
     * @param strIconKey the key
     * @return the icon name
     */
    public static String getIcon( String strProvider, String strIconKey )
    {
        try
        {
            IIconProvider iconProvider = (IIconProvider) SpringContextService.getBean( BEAN_PREFIX + strProvider );
            String icon = iconProvider.getIcon( strIconKey );

            return ( icon == null ) ? "default" : icon;
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            AppLogService.error( "icon API: Missing strProvider " + strProvider + " , strIconKey " + strIconKey +
                ", exception " + e );

            return "default";
        }
    }
}
