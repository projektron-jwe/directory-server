/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.server.core.security;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.apache.directory.server.core.entry.DefaultServerAttributeTest;
import org.apache.directory.server.core.entry.DefaultServerEntry;
import org.apache.directory.shared.ldap.schema.ldif.extractor.SchemaLdifExtractor;
import org.apache.directory.shared.ldap.constants.SchemaConstants;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.apache.directory.shared.ldap.schema.registries.Registries;
import org.apache.directory.shared.schema.loader.ldif.LdifSchemaLoader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test for the TlsKeyGenerator class.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class TlsKeyGeneratorTest
{
    private static final Logger LOG = LoggerFactory.getLogger( TlsKeyGeneratorTest.class );
    private static LdifSchemaLoader loader;
    private static Registries registries;
    

    /**
     * Initialize the registries once for the whole test suite
     */
    @BeforeClass
    public static void setup() throws Exception
    {
    	String workingDirectory = System.getProperty( "workingDirectory" );

        if ( workingDirectory == null )
        {
            String path = DefaultServerAttributeTest.class.getResource( "" ).getPath();
            int targetPos = path.indexOf( "target" );
            workingDirectory = path.substring( 0, targetPos + 6 );
        }

        File schemaRepository = new File( workingDirectory, "schema" );
        SchemaLdifExtractor extractor = new SchemaLdifExtractor( new File( workingDirectory ) );
        extractor.extractOrCopy();
        loader = new LdifSchemaLoader( schemaRepository );
        registries = new Registries();
        loader.loadAllEnabled( registries );
    }
    
    
    /**
     * Test method for all methods in one.
     */
    @Test
    public void testAll() throws Exception
    {
        DefaultServerEntry entry = new DefaultServerEntry( registries, new LdapDN() );
        TlsKeyGenerator.addKeyPair( entry );
        LOG.debug( "Entry: {}", entry );
        assertTrue( entry.contains( SchemaConstants.OBJECT_CLASS_AT, TlsKeyGenerator.TLS_KEY_INFO_OC ) );
        
        KeyPair keyPair = TlsKeyGenerator.getKeyPair( entry );
        assertNotNull( keyPair );
        
        X509Certificate cert = TlsKeyGenerator.getCertificate( entry );
        assertNotNull( cert );
    }
}
