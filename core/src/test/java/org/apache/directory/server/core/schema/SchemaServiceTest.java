/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.core.schema;


import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.directory.server.core.entry.DefaultServerAttributeTest;
import org.apache.directory.shared.ldap.schema.AttributeType;
import org.apache.directory.shared.ldap.schema.ldif.extractor.SchemaLdifExtractor;
import org.apache.directory.shared.ldap.schema.registries.AttributeTypeRegistry;
import org.apache.directory.shared.ldap.schema.registries.Registries;
import org.apache.directory.shared.schema.loader.ldif.LdifSchemaLoader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * Tests methods in SchemaInterceptor.
 */
public class SchemaServiceTest
{
    private static Registries registries;
    
    
    @Before
    public void setUp() throws Exception
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
        LdifSchemaLoader loader = new LdifSchemaLoader( schemaRepository );
        registries = new Registries();
        loader.loadAllEnabled( registries );
        loader.loadWithDependencies( loader.getSchema( "nis" ), registries );
    }

    
    @Test
    public void testDescendants() throws Exception
    {
        AttributeTypeRegistry attrRegistry = registries.getAttributeTypeRegistry();
        Iterator<AttributeType> list = attrRegistry.descendants( "name" );
        Set<String> nameAttrs = new HashSet<String>();
        
        while ( list.hasNext() )
        {
            AttributeType type = list.next();
            nameAttrs.add( type.getName() );
        }
        
        assertEquals( "size of attributes extending name", 15, nameAttrs.size() );
        assertTrue( nameAttrs.contains( "dmdName" ) );
        assertTrue( nameAttrs.contains( "o" ) );
        assertTrue( nameAttrs.contains( "c" ) );
        assertTrue( nameAttrs.contains( "initials" ) );
        assertTrue( nameAttrs.contains( "ou" ) );
        assertTrue( nameAttrs.contains( "sn" ) );
        assertTrue( nameAttrs.contains( "title" ) );
        assertTrue( nameAttrs.contains( "l" ) );
        assertTrue( nameAttrs.contains( "apacheExistence" ) );
        assertTrue( nameAttrs.contains( "cn" ) );
        assertTrue( nameAttrs.contains( "st" ) );
        assertTrue( nameAttrs.contains( "givenName" ) );
    }
/*
    public void testAlterObjectClassesBogusAttr() throws NamingException
    {
        Attribute attr = new AttributeImpl( "blah", "blah" );

        try
        {
            SchemaInterceptor.alterObjectClasses( attr, registries.getObjectClassRegistry() );
            fail( "should not get here" );
        }
        catch ( LdapNamingException e )
        {
            assertEquals( ResultCodeEnum.OPERATIONS_ERROR, e.getResultCode() );
        }

        attr = new AttributeImpl( "objectClass" );
        SchemaInterceptor.alterObjectClasses( attr );
        assertEquals( 0, attr.size() );
    }


    public void testAlterObjectClassesNoAttrValue() throws NamingException
    {
        Attribute attr = new AttributeImpl( "objectClass" );
        SchemaInterceptor.alterObjectClasses( attr );
        assertEquals( 0, attr.size() );
    }


    public void testAlterObjectClassesTopAttrValue() throws NamingException
    {
        Attribute attr = new AttributeImpl( "objectClass", "top" );
        SchemaInterceptor.alterObjectClasses( attr, registries.getObjectClassRegistry() );
        assertEquals( 0, attr.size() );
    }


    public void testAlterObjectClassesInetOrgPersonAttrValue() throws NamingException
    {
        Attribute attr = new AttributeImpl( "objectClass", "organizationalPerson" );
        SchemaInterceptor.alterObjectClasses( attr, registries.getObjectClassRegistry() );
        assertEquals( 2, attr.size() );
        assertTrue( attr.contains( "person" ) );
        assertTrue( attr.contains( "organizationalPerson" ) );
    }


    public void testAlterObjectClassesOverlapping() throws NamingException
    {
        Attribute attr = new AttributeImpl( "objectClass", "organizationalPerson" );
        attr.add( "residentialPerson" );
        SchemaInterceptor.alterObjectClasses( attr, registries.getObjectClassRegistry() );
        assertEquals( 3, attr.size() );
        assertTrue( attr.contains( "person" ) );
        assertTrue( attr.contains( "organizationalPerson" ) );
        assertTrue( attr.contains( "residentialPerson" ) );
    }


    public void testAlterObjectClassesOverlappingAndDsa() throws NamingException
    {
        Attribute attr = new AttributeImpl( "objectClass", "organizationalPerson" );
        attr.add( "residentialPerson" );
        attr.add( "dSA" );
        SchemaInterceptor.alterObjectClasses( attr, registries.getObjectClassRegistry() );
        assertEquals( 5, attr.size() );
        assertTrue( attr.contains( "person" ) );
        assertTrue( attr.contains( "organizationalPerson" ) );
        assertTrue( attr.contains( "residentialPerson" ) );
        assertTrue( attr.contains( "dSA" ) );
        assertTrue( attr.contains( "applicationEntity" ) );
    }
    */
}
